package com.voltfitness.app.ui.screens.exercise

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voltfitness.app.domain.model.Exercise
import com.voltfitness.app.domain.repository.ExerciseRepository
import com.voltfitness.app.domain.search.FuzzySearchEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ==================== STATE ====================

enum class ExerciseFilterTab { All, Muscles, Equipment }

data class ExercisePickerUiState(
    val query: String = "",
    val exercises: List<Exercise> = emptyList(),
    val allExercises: List<Exercise> = emptyList(),
    val selectedExerciseIds: Set<String> = emptySet(),
    val filterTab: ExerciseFilterTab = ExerciseFilterTab.All,
    val bodyParts: List<String> = emptyList(),
    val targets: List<String> = emptyList(),
    val equipment: List<String> = emptyList(),
    val selectedBodyPart: String? = null,
    val selectedTarget: String? = null,
    val selectedEquipment: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

// ==================== EVENTS ====================

sealed interface ExercisePickerEvent {
    data class OnQueryChanged(val query: String) : ExercisePickerEvent
    data class OnFilterTabChanged(val tab: ExerciseFilterTab) : ExercisePickerEvent
    data class OnToggleSelection(val exerciseId: String) : ExercisePickerEvent
    data class OnBodyPartSelected(val bodyPart: String?) : ExercisePickerEvent
    data class OnTargetSelected(val target: String?) : ExercisePickerEvent
    data class OnEquipmentSelected(val equipment: String?) : ExercisePickerEvent
}

// ==================== VIEWMODEL ====================

/**
 * ViewModel for the Exercise Picker screen.
 *
 * On initialization:
 * 1. Checks if local cache is empty → if yes, fetches from API
 * 2. Loads all exercises into memory for fuzzy search
 * 3. Loads catalog lists (body parts, targets, equipment) for filters
 *
 * Search uses [FuzzySearchEngine] with debounce (300ms) to avoid
 * running Levenshtein on every keystroke.
 */
@HiltViewModel
class ExercisePickerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {

    private val routineId: Long = savedStateHandle["routineId"] ?: 0L
    private val dayOrder: Int = savedStateHandle["dayOrder"] ?: 1

    private val _uiState = MutableStateFlow(ExercisePickerUiState())
    val uiState: StateFlow<ExercisePickerUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadData()
    }

    fun onEvent(event: ExercisePickerEvent) {
        when (event) {
            is ExercisePickerEvent.OnQueryChanged -> {
                _uiState.update { it.copy(query = event.query) }
                debouncedSearch(event.query)
            }
            is ExercisePickerEvent.OnFilterTabChanged -> {
                _uiState.update { it.copy(filterTab = event.tab) }
            }
            is ExercisePickerEvent.OnToggleSelection -> {
                _uiState.update { state ->
                    val newSelection = state.selectedExerciseIds.toMutableSet()
                    if (newSelection.contains(event.exerciseId)) {
                        newSelection.remove(event.exerciseId)
                    } else {
                        newSelection.add(event.exerciseId)
                    }
                    state.copy(selectedExerciseIds = newSelection)
                }
            }
            is ExercisePickerEvent.OnBodyPartSelected -> {
                _uiState.update { it.copy(selectedBodyPart = event.bodyPart) }
                applyFilters()
            }
            is ExercisePickerEvent.OnTargetSelected -> {
                _uiState.update { it.copy(selectedTarget = event.target) }
                applyFilters()
            }
            is ExercisePickerEvent.OnEquipmentSelected -> {
                _uiState.update { it.copy(selectedEquipment = event.equipment) }
                applyFilters()
            }
        }
    }

    /** Returns the list of selected exercise IDs for the caller screen. */
    fun getSelectedIds(): List<String> = _uiState.value.selectedExerciseIds.toList()

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Populate cache if empty
                if (exerciseRepository.isCacheEmpty()) {
                    exerciseRepository.refreshExercisesFromRemote()
                    exerciseRepository.refreshCatalogsFromRemote()
                }

                // Load exercises into memory
                val all = exerciseRepository.getAllExercisesList()

                // Load catalogs
                val bodyParts = exerciseRepository.getBodyParts()
                val targets = exerciseRepository.getTargets()
                val equipment = exerciseRepository.getEquipment()

                _uiState.update {
                    it.copy(
                        allExercises = all,
                        exercises = all,
                        bodyParts = bodyParts,
                        targets = targets,
                        equipment = equipment,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load exercises: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Debounced fuzzy search — waits 300ms after the user stops typing
     * before executing the search. This prevents running Levenshtein
     * on every single keystroke for 1300+ exercises.
     */
    private fun debouncedSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            val state = _uiState.value
            val baseList = getFilteredBaseList(state)

            val results = if (query.isBlank()) {
                baseList
            } else {
                FuzzySearchEngine.search(
                    query = query,
                    exercises = baseList
                )
            }

            _uiState.update { it.copy(exercises = results) }
        }
    }

    private fun applyFilters() {
        viewModelScope.launch {
            val state = _uiState.value
            val baseList = getFilteredBaseList(state)

            val results = if (state.query.isBlank()) {
                baseList
            } else {
                FuzzySearchEngine.search(query = state.query, exercises = baseList)
            }

            _uiState.update { it.copy(exercises = results) }
        }
    }

    /** Applies category filters (body part, target, equipment) to the full list. */
    private fun getFilteredBaseList(state: ExercisePickerUiState): List<Exercise> {
        return state.allExercises.filter { exercise ->
            val matchesBodyPart = state.selectedBodyPart == null ||
                    exercise.bodyPart.equals(state.selectedBodyPart, ignoreCase = true)
            val matchesTarget = state.selectedTarget == null ||
                    exercise.target.equals(state.selectedTarget, ignoreCase = true)
            val matchesEquipment = state.selectedEquipment == null ||
                    exercise.equipment.equals(state.selectedEquipment, ignoreCase = true)

            matchesBodyPart && matchesTarget && matchesEquipment
        }
    }
}
