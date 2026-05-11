package com.voltfitness.app.ui.screens.routine

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voltfitness.app.R
import com.voltfitness.app.domain.model.Routine
import com.voltfitness.app.domain.model.RoutineDay
import com.voltfitness.app.domain.model.RoutineExercise
import com.voltfitness.app.domain.repository.ExerciseRepository
import com.voltfitness.app.domain.usecase.routine.DeleteRoutineUseCase
import com.voltfitness.app.domain.usecase.routine.GetRoutineDetailUseCase
import com.voltfitness.app.domain.usecase.routine.SaveRoutineUseCase
import com.voltfitness.app.ui.common.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * ## Modes
 * - **Create** (`routineId == -1L`): starts with one empty day.
 * - **Edit** (`routineId > 0`): loads routine + days + exercises from DB via
 *   [GetRoutineDetailUseCase], which returns a [RoutineWithFullDaysDomain].
 *
 * ## Exercise selection flow
 * 1. User taps "Add exercise" on a day → navigates to ExercisePicker.
 * 2. Picker returns `List<String>` of ExerciseDB IDs + the target `dayIndex`.
 * 3. [handleExercisesSelected] resolves full [Exercise] objects from
 *    [ExerciseRepository] and appends them as [RoutineExerciseUi] entries.
 *
 * ## Save flow
 * [saveRoutine] maps UI state → domain models and calls [SaveRoutineUseCase],
 * which resolves the real DB IDs after upsert before persisting exercises.
 */
@HiltViewModel
class RoutineEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRoutineDetailUseCase: GetRoutineDetailUseCase,
    private val saveRoutineUseCase: SaveRoutineUseCase,
    private val deleteRoutineUseCase: DeleteRoutineUseCase,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val routineId: Long = savedStateHandle["routineId"] ?: -1L
    private val folderId: Long = savedStateHandle["folderId"] ?: -1L
    private val isNew = routineId == -1L

    private val _uiState = MutableStateFlow(
        RoutineEditorUiState(
            routineId = if (isNew) -1L else routineId,
            folderId = folderId,
            isNewRoutine = isNew,
        )
    )
    val uiState: StateFlow<RoutineEditorUiState> = _uiState.asStateFlow()

    val isFormValid: StateFlow<Boolean> = _uiState
        .map { it.routineName.isNotBlank() && it.description.isNotBlank() && it.goal.isNotBlank() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private val _effect = Channel<RoutineEditorEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        savedStateHandle.getStateFlow<List<String>?>("exercise_selection_result", null)
            .filterNotNull()
            .onEach { exerciseIds ->
                val dayIndex = _uiState.value.expandedDayIndex
                if (dayIndex != -1) {
                    handleExercisesSelected(exerciseIds, dayIndex)
                }
                savedStateHandle["exercise_selection_result"] = null
            }
            .launchIn(viewModelScope)

        if (isNew) {
            _uiState.update {
                it.copy(
                    days = listOf(
                        RoutineDayUi(
                            order = 1,
                            name = UiText.StringResource(R.string.day_1)
                        )
                    )
                )
            }
        } else {
            loadExistingRoutine(routineId)
        }
    }


    /**
     * Loads routine metadata, its days, and the full exercise list for each day.
     *
     * [GetRoutineDetailUseCase] returns a [RoutineWithFullDaysDomain] which
     * carries [RoutineDayFullDomain] entries — each containing the day metadata
     * and a list of [RoutineExerciseWithSetsDomain].
     *
     * Each [RoutineExerciseWithSetsDomain.routineExercise] is a [RoutineExercise]
     * domain model; we map it to [RoutineExerciseUi] using the stored
     * [RoutineExercise.exerciseId] (the ExerciseDB string ID) so that
     * [ExerciseGifImage] can load the correct GIF.
     */
    private fun loadExistingRoutine(routineId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val detail = getRoutineDetailUseCase(routineId).first() ?: return@launch

                val updatedDays = detail.days.map { dayFull ->
                    RoutineDayUi(
                        id = dayFull.day.id,
                        order = dayFull.day.dayOrder,
                        name = UiText.DynamicString(dayFull.day.name),
                        exercises = dayFull.exercises.map { exerciseWithSets ->
                            val re = exerciseWithSets.routineExercise
                            RoutineExerciseUi(
                                routineExerciseId = re.routineExerciseId,
                                exerciseDbId = re.exerciseId,
                                name = re.exerciseName.ifBlank { re.exerciseId },
                                sets = re.sets,
                                repsRange = re.repsRange,
                                weightRange = re.weightRange
                            )
                        }
                    )
                }.ifEmpty {
                    listOf(RoutineDayUi(order = 1, name = UiText.StringResource(R.string.day_1)))
                }

                _uiState.update { state ->
                    state.copy(
                        routineId = detail.routine.id,
                        folderId = detail.routine.folderId,
                        routineName = detail.routine.name,
                        description = detail.routine.description ?: "",
                        goal = detail.routine.goal ?: "",
                        isMainRoutine = detail.routine.isActive,
                        isNewRoutine = false,
                        days = updatedDays,
                        createdAt = detail.routine.createdAt
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to load selected exercises")
                _effect.send(
                    RoutineEditorEffect.ShowError(
                        UiText.StringResource(R.string.failed_to_add_exercises, e.message ?: "")
                    )
                )
            }
        }
    }

    fun onEvent(event: RoutineEditorEvent) {
        when (event) {
            is RoutineEditorEvent.OnNameChanged ->
                _uiState.update { it.copy(routineName = event.name) }

            is RoutineEditorEvent.OnDescriptionChanged ->
                _uiState.update { it.copy(description = event.description) }

            is RoutineEditorEvent.OnGoalChanged ->
                _uiState.update { it.copy(goal = event.goal) }

            is RoutineEditorEvent.OnToggleMainRoutine ->
                _uiState.update { it.copy(isMainRoutine = !it.isMainRoutine) }

            is RoutineEditorEvent.OnToggleDayExpanded ->
                _uiState.update { state ->
                    state.copy(
                        expandedDayIndex = if (state.expandedDayIndex == event.index) -1
                        else event.index
                    )
                }

            is RoutineEditorEvent.OnAddDay ->
                _uiState.update { state ->
                    val newOrder = state.days.size + 1
                    state.copy(
                        days = state.days + RoutineDayUi(
                            order = newOrder,
                            name = UiText.StringResource(R.string.day, newOrder)
                        ),
                        expandedDayIndex = state.days.size
                    )
                }

            is RoutineEditorEvent.OnRemoveDay ->
                _uiState.update { state ->
                    if (state.days.size <= 1) return@update state
                    val updated = state.days
                        .toMutableList()
                        .apply { removeAt(event.index) }
                        .mapIndexed { idx, day -> day.copy(order = idx + 1) }
                    state.copy(
                        days = updated,
                        expandedDayIndex = if (state.expandedDayIndex >= updated.size)
                            updated.size - 1 else state.expandedDayIndex
                    )
                }

            is RoutineEditorEvent.OnDayNameChanged ->
                _uiState.update { state ->
                    state.copy(
                        days = state.days.toMutableList().apply {
                            this[event.index] = this[event.index].copy(
                                name = UiText.DynamicString(event.name)
                            )
                        }
                    )
                }

            is RoutineEditorEvent.OnSave -> saveRoutine()
            is RoutineEditorEvent.OnDelete -> deleteRoutine()

            is RoutineEditorEvent.OnExercisesSelectedForDay ->
                viewModelScope.launch {
                    handleExercisesSelected(event.exerciseIds, event.dayIndex)
                }
        }
    }


    /**
     * Resolves the full [Exercise] domain model for each selected ID and
     * appends them as [RoutineExerciseUi] entries to the target day.
     *
     * - Preserves the original selection order from the picker.
     * - Exercises that cannot be found in the local cache are silently skipped
     *   (they were never downloaded); this should not happen in practice.
     *
     * @param exerciseIds ExerciseDB string IDs to add.
     * @param dayIndex    Index of the target day in [RoutineEditorUiState.days].
     */
    private suspend fun handleExercisesSelected(exerciseIds: List<String>, dayIndex: Int) {

        val state = _uiState.value
        if (dayIndex !in state.days.indices) return

        try {
            withContext(Dispatchers.IO) {

                val exerciseMap = exerciseRepository.getExercisesByIds(exerciseIds)
                    .associateBy { it.id }

                val newExercises = exerciseIds.mapNotNull { id ->
                    exerciseMap[id]?.let { exercise ->
                        RoutineExerciseUi(
                            exerciseDbId = exercise.id,
                            name = exercise.name.replaceFirstChar { it.uppercase() }
                        )
                    }
                }

                _uiState.update { current ->
                    val currentDay = current.days[dayIndex]
                    current.copy(
                        days = current.days.toMutableList().apply {
                            this[dayIndex] = currentDay.copy(
                                exercises = currentDay.exercises + newExercises
                            )
                        }
                    )
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to load selected exercises")
            _effect.send(RoutineEditorEffect.ShowError(UiText.StringResource(R.string.failed_to_add_exercises)))
        }
    }


    /**
     * Maps the current UI state to domain models and calls [SaveRoutineUseCase].
     *
     * Each day is paired with its exercise list so [SaveRoutineUseCase] can
     * resolve the real dayIds post-upsert before persisting the exercises.
     */
    private fun saveRoutine() {
        val state = _uiState.value
        if (state.routineName.isBlank()) {
            viewModelScope.launch {
                _effect.send(RoutineEditorEffect.ShowError(UiText.StringResource(R.string.routine_name_cannot_be_empty)))
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val routine = Routine(
                    id = if (state.isNewRoutine) 0L else state.routineId,
                    folderId = state.folderId,
                    name = state.routineName.trim(),
                    description = state.description.trim().ifBlank { null },
                    goal = state.goal.trim().ifBlank { null },
                    isActive = state.isMainRoutine,
                    daysPerWeek = state.days.size,
                    createdAt = state.createdAt ?: System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                val daysWithExercises = state.days.map { dayUi ->
                    val day = RoutineDay(
                        id = dayUi.id,
                        routineId = if (state.isNewRoutine) 0L else state.routineId,
                        dayOrder = dayUi.order,
                        name = dayUi.name.asRawString()
                    )
                    val exercises = dayUi.exercises.mapIndexed { index, exUi ->
                        RoutineExercise(
                            routineExerciseId = exUi.routineExerciseId,
                            routineId = day.routineId,
                            dayId = day.id,
                            exerciseId = exUi.exerciseDbId,
                            exerciseName = exUi.name,
                            orderInDay = index + 1,
                            sets = exUi.sets,
                            repsRange = exUi.repsRange,
                            weightRange = exUi.weightRange,
                            notes = null
                        )
                    }
                    day to exercises
                }

                saveRoutineUseCase(routine, daysWithExercises)
                _effect.send(RoutineEditorEffect.NavigateBack)
            } catch (e: Exception) {
                Timber.e(e, "Error saving routine")
                _effect.send(
                    RoutineEditorEffect.ShowError(
                        UiText.StringResource(R.string.failed_to_save, e.message ?: "")
                    )
                )
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }


    private fun deleteRoutine() {
        val state = _uiState.value
        if (state.isNewRoutine) return

        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }
            try {
                deleteRoutineUseCase(state.routineId)
                _effect.send(RoutineEditorEffect.NavigateBack)
            } catch (e: Exception) {
                _effect.send(
                    RoutineEditorEffect.ShowError(
                        UiText.StringResource(
                            resId = R.string.failed_to_delete,
                            args = listOf(
                                e.message ?: UiText.StringResource(R.string.unknown_error)
                            )
                        )
                    )
                )
            } finally {
                _uiState.update { it.copy(isDeleting = false) }
            }
        }
    }

    private fun UiText.asRawString(): String {
        return when (this) {
            is UiText.DynamicString -> value
            is UiText.Empty -> ""
            is UiText.StringResource -> "Day"
        }
    }
}