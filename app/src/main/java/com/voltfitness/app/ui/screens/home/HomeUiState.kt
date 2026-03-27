package com.voltfitness.app.ui.screens.home

import com.voltfitness.app.domain.model.ProgressStatus
import com.voltfitness.app.domain.model.User
import com.voltfitness.app.domain.relations.FolderWithRoutinesDomain
import com.voltfitness.app.ui.components.cards.ExerciseSummary

/**
 * Immutable UI state for the Home screen dashboard.
 *
 * Weight-related fields ([currentWeight], [primaryChangeText], etc.) are
 * pre-formatted strings ready for display by [WeightCard].
 */
data class HomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,

    val user: User? = null,

    // Weight card data
    val currentWeight: String = "",
    val weightUnit: String = "kg",
    val lastWeightUpdated: String = "No records yet",
    val primaryChangeText: String = "Welcome!",
    val secondaryChangeText: String? = "Log your weight to get started",
    val progressStatus: ProgressStatus = ProgressStatus.NEUTRAL,
    val compositionScore: Float? = null,

    // Dashboard sections
    val suggestedWorkouts: List<ExerciseSummary> = emptyList(),
    val folders: List<FolderWithRoutinesDomain> = emptyList()
)
