package com.voltfitness.app.domain.model

import java.time.LocalDate

data class BodyCompositionEntry(
    val entryId: Long,
    val userId: Long,
    val date: LocalDate,

    val heightCm: Float,
    val weightKg: Float,
    val bodyFatPercent: Float?,
    val waterPercent: Float?,
    val muscleMassKg: Float?,
    val visceralFatPercent: Float?,
    val basalCalories: Int?,
    val metabolicAge: Int?,
    val boneMassKg: Float?,

    val chestCm: Float?,
    val waistCm: Float?,
    val hipCm: Float?,
    val gluteCm: Float?,
    val leftArmCm: Float?,
    val rightArmCm: Float?,
    val leftLegCm: Float?,
    val rightLegCm: Float?,

    val fatMassKg: Float?,
    val leanMassKg: Float?,
    val ffmi: Float?,
    val waistHipRatio: Float?,
    val compositionScore: Float?
)