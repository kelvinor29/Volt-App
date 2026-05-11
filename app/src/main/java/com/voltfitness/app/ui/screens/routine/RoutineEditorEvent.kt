package com.voltfitness.app.ui.screens.routine

import com.voltfitness.app.R
import com.voltfitness.app.ui.common.UiText

/**
 * UI-specific representation of an exercise within the editor context.
 *
 * Maps catalog data into an editable format for routine construction.
 */
data class RoutineExerciseUi(
    val routineExerciseId: Long = 0L,
    val exerciseDbId: String = "",
    val name: String = "",
    val bodyPart: String? = null,
    val target: String? = null,
    val equipment: String? = null,
    val sets: Int = 3,
    val repsRange: String = "8-10",
    val weightRange: String = "10-15 kg",
    val notes: String? = null
)

/**
 * UI-specific representation of a training day.
 *
 * Aggregates a collection of [RoutineExerciseUi] in a specific execution order.
 */
data class RoutineDayUi(
    val id: Long = 0L,
    val order: Int = 1,
    val name: UiText = UiText.StringResource(R.string.day_1),
    val exercises: List<RoutineExerciseUi> = emptyList()
)

/**
 * Aggregate UI state for the Routine Editor screen.
 *
 * Encapsulates form data, loading states, and validation logic.
 */
data class RoutineEditorUiState(
    val routineId: Long = -1L,
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
    val error: UiText? = null,
    val createdAt: Long? = null,
)

/**
 * User interactions and commands for the Routine Editor.
 */
sealed interface RoutineEditorEvent {
    // --- Metadata Updates ---
    data class OnNameChanged(val name: String) : RoutineEditorEvent
    data class OnDescriptionChanged(val description: String) : RoutineEditorEvent
    data class OnGoalChanged(val goal: String) : RoutineEditorEvent
    data object OnToggleMainRoutine : RoutineEditorEvent

    // --- Day Management ---
    data class OnToggleDayExpanded(val index: Int) : RoutineEditorEvent
    data object OnAddDay : RoutineEditorEvent
    data class OnRemoveDay(val index: Int) : RoutineEditorEvent
    data class OnDayNameChanged(val index: Int, val name: String) : RoutineEditorEvent

    // --- Exercise Management ---
    data class OnExercisesSelectedForDay(
        val exerciseIds: List<String>,
        val dayIndex: Int
    ) : RoutineEditorEvent

    // --- Lifecycle Actions ---
    data object OnSave : RoutineEditorEvent
    data object OnDelete : RoutineEditorEvent
}

/**
 * One-shot navigation or UI notifications triggered by the ViewModel.
 */
sealed interface RoutineEditorEffect {
    data object NavigateBack : RoutineEditorEffect
    data class ShowError(val message: UiText) : RoutineEditorEffect
    data class ShowSuccess(val message: String) : RoutineEditorEffect
}