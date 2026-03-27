package com.voltfitness.app.ui.screens.body_composition


import java.time.LocalDate

/**
 * UI state for the BodyCompositionScreen, holding loading state, user info, and composition data.
 */
data class BodyCompositionUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val gender: String? = null,
    val goal: String? = null,
    val latestEntry: BodyCompositionDisplayItem? = null,
    val historyEntries: List<BodyCompositionDisplayItem> = emptyList(),
)

/**
 * Display model for body composition entries, used in UI components.
 * Contains all metrics from BodyCompositionEntry plus formatting convenience.
 */
data class BodyCompositionDisplayItem(
    val entryId: Long = 0,
    val date: LocalDate = LocalDate.now(),

    // Body composition data
    val weightKg: Float = 0f,
    val bodyFatPercent: Float? = null,
    val waterPercent: Float? = null,
    val muscleMassKg: Float? = null,
    val visceralFatPercent: Float? = null,
    val basalCalories: Int? = null,
    val metabolicAge: Int? = null,
    val boneMassKg: Float? = null,

    // Body measurements
    val chestCm: Float? = null,
    val waistCm: Float? = null,
    val hipCm: Float? = null,
    val gluteCm: Float? = null,
    val leftArmCm: Float? = null,
    val rightArmCm: Float? = null,
    val leftLegCm: Float? = null,
    val rightLegCm: Float? = null,

    // Calculated metrics
    val fatMassKg: Float? = null,
    val leanMassKg: Float? = null,
    val ffmi: Float? = null,
    val waistHipRatio: Float? = null,
    val compositionScore: Float? = null
)