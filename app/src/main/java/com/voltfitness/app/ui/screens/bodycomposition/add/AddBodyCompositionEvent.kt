package com.voltfitness.app.ui.screens.bodycomposition.add

import java.time.LocalDate

/**
 * Intent-based events for AddBodyCompositionScreen.
 * Only body-metric and measurement events remain.
 */
sealed interface AddBodyCompositionEvent {
    // ── Body Composition ──
    data class UpdateHeight(val value: String) : AddBodyCompositionEvent
    data class UpdateWeight(val value: String) : AddBodyCompositionEvent
    data class UpdateBodyFat(val value: String) : AddBodyCompositionEvent
    data class UpdateWater(val value: String) : AddBodyCompositionEvent
    data class UpdateMuscleMass(val value: String) : AddBodyCompositionEvent
    data class UpdateVisceralFat(val value: String) : AddBodyCompositionEvent
    data class UpdateBasalCalories(val value: String) : AddBodyCompositionEvent
    data class UpdateMetabolicAge(val value: String) : AddBodyCompositionEvent
    data class UpdateBoneMass(val value: String) : AddBodyCompositionEvent

    // ── Body Measurements ──
    data class UpdateChest(val value: String) : AddBodyCompositionEvent
    data class UpdateWaist(val value: String) : AddBodyCompositionEvent
    data class UpdateHip(val value: String) : AddBodyCompositionEvent
    data class UpdateGlute(val value: String) : AddBodyCompositionEvent
    data class UpdateLeftArm(val value: String) : AddBodyCompositionEvent
    data class UpdateRightArm(val value: String) : AddBodyCompositionEvent
    data class UpdateLeftLeg(val value: String) : AddBodyCompositionEvent
    data class UpdateRightLeg(val value: String) : AddBodyCompositionEvent

    // ── General ──
    data class UpdateDate(val date: LocalDate) : AddBodyCompositionEvent
    data object Save : AddBodyCompositionEvent
    data object DismissError : AddBodyCompositionEvent
}
