package com.voltfitness.app.ui.screens.routine

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voltfitness.app.domain.model.Exercise
import com.voltfitness.app.domain.model.Routine
import com.voltfitness.app.domain.model.RoutineDay
import com.voltfitness.app.domain.repository.ExerciseRepository
import com.voltfitness.app.domain.usecase.routine.DeleteRoutineUseCase
import com.voltfitness.app.domain.usecase.routine.GetRoutineDetailUseCase
import com.voltfitness.app.domain.usecase.routine.SaveRoutineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// =============================================================================
// UI STATE
// =============================================================================

data class RoutineDayUi(
    val id: Long = 0L,
    val order: Int = 1,
    val name: String = "Day 1",
    val exercises: List<Exercise> = emptyList()
)

/**
 * Complete UI state for the Routine Editor screen.
 */
data class RoutineEditorUiState(
    val routineId: Long = 0L,
    val folderId: Long = 0L,
    val routineName: String = "",
    val description: String = "",
    val goal: String = "",
    val isMainRoutine: Boolean = false,
    val days: List<RoutineDayUi> = emptyList(),
    val expandedDayIndex: Int = 0,
    val isNewRoutine: Boolean = true,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val error: String,
) {
    val isFormValid: Boolean get() = routineName.isNotBlank() && description.isNotBlank() && goal.isNotBlank()
}

// =============================================================================
// EVENTS
// =============================================================================

/**
 * All user-driven actions on the Routine Editor.
 */
sealed interface RoutineEditorEvent {
    data class OnNameChanged(val name: String) : RoutineEditorEvent
    data class OnDescriptionChanged(val description: String) : RoutineEditorEvent
    data class OnGoalChanged(val goal: String) : RoutineEditorEvent
    data object OnToggleMainRoutine : RoutineEditorEvent
    data class OnToggleDayExpanded(val index: Int) : RoutineEditorEvent
    data object OnAddDay : RoutineEditorEvent
    data class OnRemoveDay(val index: Int) : RoutineEditorEvent
    data class OnDayNameChanged(val index: Int, val name: String) : RoutineEditorEvent
    data object OnSave : RoutineEditorEvent
    data object OnDelete : RoutineEditorEvent
    data class OnExercisesSelectedForDay(val exerciseIds: List<String>) : RoutineEditorEvent
}

/**
 * One-time navigation/UI effects emitted by the ViewModel.
 */
sealed interface RoutineEditorEffect {
    data object NavigateBack : RoutineEditorEffect
    data class ShowError(val message: String) : RoutineEditorEffect
    data class ShowSuccess(val message: String) : RoutineEditorEffect
}

// =============================================================================
// VIEWMODEL
// =============================================================================

/**
 * ViewModel managing the Routine Editor's state and CRUD operations.
 *
 * Navigation arguments extracted from [SavedStateHandle]:
 * - "folderId" (Long): Folder where a new routine will be created.
 * - "routineId" (Long): -1L for creation mode, or a numeric ID for edit mode.
 *
 * In edit mode, the existing routine data is loaded from the database
 * and populates the form fields reactively.
 *
 * When the user confirms an exercise selection from the Exercise Picker,
 * [handleExercisesSelected] resolves the full [Exercise] objects from the
 * repository so that [RoutineExerciseUi] always holds real data (name, ID)
 * instead of placeholders.
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
            error = ""
        )
    )
    val uiState: StateFlow<RoutineEditorUiState> = _uiState.asStateFlow()

    private val _effect = Channel<RoutineEditorEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        if (isNew) {
            _uiState.update {
                it.copy(days = listOf(RoutineDayUi(order = 1, name = "Day 1")))
            }
        } else {
            loadExistingRoutine(routineId)
        }
    }

    /** Loads routine data from the database and maps it to UI state. */
    private fun loadExistingRoutine(routineId: Long) {
        viewModelScope.launch {
            getRoutineDetailUseCase(routineId).collect { detail ->
                detail?.let { data ->
                    _uiState.update { state ->
                        state.copy(
                            routineId = data.routine.id,
                            folderId = data.routine.folderId,
                            routineName = data.routine.name,
                            description = data.routine.description ?: "",
                            goal = data.routine.goal ?: "",
                            isMainRoutine = data.routine.isActive,
                            isNewRoutine = false,
                            days = data.days.map { day ->
                                RoutineDayUi(
                                    id = day.id,
                                    order = day.dayOrder,
                                    name = day.name
                                )
                            }.ifEmpty {
                                listOf(RoutineDayUi(order = 1, name = "Day 1"))
                            }
                        )
                    }
                }
            }
        }
    }

    /** Central event handler — unidirectional data flow. */
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

            is RoutineEditorEvent.OnAddDay -> {
                _uiState.update { state ->
                    val newOrder = state.days.size + 1
                    state.copy(
                        days = state.days + RoutineDayUi(
                            order = newOrder,
                            name = "Day $newOrder"
                        ),
                        expandedDayIndex = state.days.size
                    )
                }
            }

            is RoutineEditorEvent.OnRemoveDay -> {
                _uiState.update { state ->
                    if (state.days.size <= 1) return@update state
                    val updatedDays = state.days
                        .toMutableList()
                        .apply { removeAt(event.index) }
                        .mapIndexed { idx, day -> day.copy(order = idx + 1) }
                    state.copy(
                        days = updatedDays,
                        expandedDayIndex = if (state.expandedDayIndex >= updatedDays.size)
                            updatedDays.size - 1 else state.expandedDayIndex
                    )
                }
            }

            is RoutineEditorEvent.OnDayNameChanged -> {
                _uiState.update { state ->
                    state.copy(
                        days = state.days.toMutableList().apply {
                            this[event.index] = this[event.index].copy(name = event.name)
                        }
                    )
                }
            }

            is RoutineEditorEvent.OnSave -> saveRoutine()
            is RoutineEditorEvent.OnDelete -> deleteRoutine()

            is RoutineEditorEvent.OnExercisesSelectedForDay -> {
                viewModelScope.launch {
                    addExercisesToCurrentDay(event.exerciseIds)
                }
            }
        }
    }

    private suspend fun addExercisesToCurrentDay(ids: List<String>) {
        val expandedIndex = _uiState.value.expandedDayIndex
        if (expandedIndex == -1) return

        try {
            val selectedExercises = exerciseRepository.getExercisesByIds(ids)

            _uiState.update { currentState ->
                val updatedDays = currentState.days.toMutableList()
                val targetDay = updatedDays[expandedIndex]

                updatedDays[expandedIndex] = targetDay.copy(
                    exercises = targetDay.exercises + selectedExercises
                )

                currentState.copy(days = updatedDays)
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Failed to load exercises: ${e.message}") }
        }
    }

    private fun saveRoutine() {
        val state = _uiState.value
        if (state.routineName.isBlank()) {
            viewModelScope.launch {
                _effect.send(RoutineEditorEffect.ShowError("Routine name cannot be empty"))
            }
            return
        }

        viewModelScope.launch {
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
                    createdAt = if (state.isNewRoutine) System.currentTimeMillis()
                    else state.routineId,
                    updatedAt = System.currentTimeMillis()
                )

                val days = state.days.map { dayUi ->
                    RoutineDay(
                        id = dayUi.id,
                        routineId = 0L,
                        dayOrder = dayUi.order,
                        name = dayUi.name.trim()
                    )
                }

                saveRoutineUseCase(routine, days)
                _effect.send(RoutineEditorEffect.NavigateBack)
            } catch (e: Exception) {
                _effect.send(RoutineEditorEffect.ShowError("Failed to save: ${e.message}"))
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
                _effect.send(RoutineEditorEffect.ShowError("Failed to delete: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isDeleting = false) }
            }
        }
    }
}