package com.voltfitness.app.ui.screens.home

import com.voltfitness.app.domain.model.ProgressStatus
import com.voltfitness.app.domain.model.User
import com.voltfitness.app.domain.relations.FolderWithRoutinesDomain
import com.voltfitness.app.ui.components.cards.ExerciseSummary

/**
 * Aggregate UI state for the Home screen dashboard.
 *
 * Weight-related fields are pre-formatted strings produced by the ViewModel
 * to ensure the UI remains logic-free and strictly responsible for rendering.
 *
 * @property isLoading Global loading state for the dashboard.
 * @property user Current authenticated user profile.
 * @property currentWeight Formatted weight value (e.g., "85.5").
 * @property weightUnit Canonical unit for the current user (kg/lbs).
 * @property progressStatus Visual trend indicator used by [WeightCard] for semantic coloring.
 * @property suggestedWorkouts List of featured exercises for the horizontal scroll section.
 * @property folders Routine collections grouped by user-defined folders.
 */
data class HomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,

    // --- User Context ---
    val user: User? = null,

    // --- Weight Card Data ---
    val currentWeight: String = "",
    val weightUnit: String = "kg",
    val lastWeightUpdated: String = "No records yet",
    val primaryChangeText: String = "Welcome!",
    val secondaryChangeText: String? = "Log your weight to get started",
    val progressStatus: ProgressStatus = ProgressStatus.NEUTRAL,
    val compositionScore: Float? = null,

    // --- Dashboard Sections ---
    val suggestedWorkouts: List<ExerciseSummary> = emptyList(),
    val folders: List<FolderWithRoutinesDomain> = emptyList(),

    // --- Active Routine Days ---
    val activeRoutineDays: List<ActiveRoutineDayUi> = emptyList()
)

/**
 * Lightweight UI model representing a single training day of the active routine.
 *
 * @param dayId        Unique identifier for navigation / click callbacks.
 * @param dayOrder     1-based day number displayed on the card header.
 * @param name         Human-readable name of the day (e.g. "Leg Day").
 * @param focusBodyParts Comma-separated muscle groups shown as the card subtitle.
 */
data class ActiveRoutineDayUi(
    val dayId: Long,
    val dayOrder: Int,
    val name: String,
    val focusBodyParts: String
)