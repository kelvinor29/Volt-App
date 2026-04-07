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
    val folders: List<FolderWithRoutinesDomain> = emptyList()
)