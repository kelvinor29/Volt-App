package com.voltfitness.app.ui.screens.bodycomposition.add

import java.time.LocalDate

/**
 * UI state for the body composition entry form.
 *
 * Uses [String] for numeric inputs to ensure smooth text editing and decimal handling
 * in Compose. Validation logic is encapsulated within computed properties.
 */
data class AddBodyCompositionUiState(
    val date: LocalDate = LocalDate.now(),

    // --- Form Control ---
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null,

    // --- Body Composition (Scale Metrics) ---
    val heightCm: String = "",
    val weightKg: String = "",
    val bodyFatPercent: String = "",
    val waterPercent: String = "",
    val muscleMassKg: String = "",
    val visceralFatPercent: String = "",
    val basalCalories: String = "",
    val metabolicAge: String = "",
    val boneMassKg: String = "",

    // --- Body Measurements (Tape Metrics) ---
    val chestCm: String = "",
    val waistCm: String = "",
    val hipCm: String = "",
    val gluteCm: String = "",
    val leftArmCm: String = "",
    val rightArmCm: String = "",
    val leftLegCm: String = "",
    val rightLegCm: String = ""
) {
    /** Validates that weight is a positive numeric value. */
    val isWeightValid: Boolean
        get() = weightKg.isNotBlank() && (weightKg.toFloatOrNull() ?: 0f) > 0f

    /** Validates that height is a positive numeric value. */
    val isHeightValid: Boolean
        get() = heightCm.isNotBlank() && (heightCm.toFloatOrNull() ?: 0f) > 0f

    /** Mandatory field compliance check. */
    val isFormValid: Boolean get() = isWeightValid && isHeightValid
}

/**
 * One-shot navigation and UI side-effects for the Add Body Composition flow.
 */
sealed interface AddBodyCompositionNavEffect {
    /** Signals a successful persistence operation. */
    data object SavedSuccessfully : AddBodyCompositionNavEffect
}