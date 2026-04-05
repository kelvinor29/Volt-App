package com.voltfitness.app.ui.screens.bodycomposition.add

import java.time.LocalDate

/**
 * Represents user intents and input events for the Body Composition entry form.
 *
 * This sealed interface defines all possible interactions, from numeric metric
 * updates to form lifecycle actions like saving or dismissing errors.
 */
sealed interface AddBodyCompositionEvent {

    // ─── Scale-Based Metrics ───
    // These events correspond to data typically retrieved from a body composition scale.

    data class UpdateHeight(val value: String) : AddBodyCompositionEvent
    data class UpdateWeight(val value: String) : AddBodyCompositionEvent
    data class UpdateBodyFat(val value: String) : AddBodyCompositionEvent
    data class UpdateWater(val value: String) : AddBodyCompositionEvent
    data class UpdateMuscleMass(val value: String) : AddBodyCompositionEvent
    data class UpdateVisceralFat(val value: String) : AddBodyCompositionEvent
    data class UpdateBasalCalories(val value: String) : AddBodyCompositionEvent
    data class UpdateMetabolicAge(val value: String) : AddBodyCompositionEvent
    data class UpdateBoneMass(val value: String) : AddBodyCompositionEvent

    // ─── Manual Tape Measurements ───
    // These events correspond to anthropometric measurements taken manually.

    data class UpdateChest(val value: String) : AddBodyCompositionEvent
    data class UpdateWaist(val value: String) : AddBodyCompositionEvent
    data class UpdateHip(val value: String) : AddBodyCompositionEvent
    data class UpdateGlute(val value: String) : AddBodyCompositionEvent
    data class UpdateLeftArm(val value: String) : AddBodyCompositionEvent
    data class UpdateRightArm(val value: String) : AddBodyCompositionEvent
    data class UpdateLeftLeg(val value: String) : AddBodyCompositionEvent
    data class UpdateRightLeg(val value: String) : AddBodyCompositionEvent

    // ─── Form Lifecycle & General ───

    /** Updates the reference date for the current entry. */
    data class UpdateDate(val date: LocalDate) : AddBodyCompositionEvent

    /** Signals the intent to persist the current form data. */
    data object Save : AddBodyCompositionEvent

    /** Resets the error state in the UI. */
    data object DismissError : AddBodyCompositionEvent
}