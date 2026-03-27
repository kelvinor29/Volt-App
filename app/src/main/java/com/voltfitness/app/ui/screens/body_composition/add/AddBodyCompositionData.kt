package com.voltfitness.app.ui.screens.body_composition.add

import java.time.LocalDate

/**
 * UI state for the AddBodyCompositionScreen form.
 * Holds raw string inputs for body metrics and tape measurements.
 *
 * Profile data (name, gender, etc.) is NOT managed here —
 * it lives in the ProfileScreen / ProfileViewModel.
 */
data class AddBodyCompositionUiState(
    val date: LocalDate = LocalDate.now(),

    // Form control
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val errorMessage: String? = null,

    // ── Body Composition (scale metrics) ──
    val heightCm: String = "",
    val weightKg: String = "",
    val bodyFatPercent: String = "",
    val waterPercent: String = "",
    val muscleMassKg: String = "",
    val visceralFatPercent: String = "",
    val basalCalories: String = "",
    val metabolicAge: String = "",
    val boneMassKg: String = "",

    // ── Body Measurements (tape metrics) ──
    val chestCm: String = "",
    val waistCm: String = "",
    val hipCm: String = "",
    val gluteCm: String = "",
    val leftArmCm: String = "",
    val rightArmCm: String = "",
    val leftLegCm: String = "",
    val rightLegCm: String = ""
) {
    val isWeightValid: Boolean
        get() = weightKg.isNotBlank() &&
                weightKg.toFloatOrNull() != null &&
                (weightKg.toFloatOrNull() ?: 0f) > 0f

    val isHeightValid: Boolean
        get() = heightCm.isNotBlank() &&
                heightCm.toFloatOrNull() != null &&
                (heightCm.toFloatOrNull() ?: 0f) > 0f

    /** True when mandatory fields are valid and ready to save. */
    val isFormValid: Boolean get() = isWeightValid && isHeightValid
}
