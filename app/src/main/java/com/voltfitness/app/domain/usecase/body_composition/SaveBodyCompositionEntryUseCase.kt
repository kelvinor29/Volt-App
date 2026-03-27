package com.voltfitness.app.domain.usecase.body_composition

import com.voltfitness.app.domain.model.BodyCompositionEntry
import com.voltfitness.app.domain.repository.BodyCompositionRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.pow

/**
 * Use Case responsible for processing and persisting raw body metrics.
 *
 * It performs derived biometric calculations (Fat Mass, Lean Mass, FFMI, WHR)
 * and generates a health score before saving the entry to the repository.
 */
class SaveBodyCompositionEntryUseCase @Inject constructor(
    private val repository: BodyCompositionRepository
) {

    /**
     * Processes the [rawEntry], calculates derived health metrics, and saves it.
     *
     * @param rawEntry The raw data captured from the user interface.
     */
    suspend operator fun invoke(rawEntry: RawBodyCompositionInput) {
        val heightM = rawEntry.heightCm / 100f

        // Derived InBody-style calculations
        val fatMassKg = rawEntry.bodyFatPercent?.let { rawEntry.weightKg * (it / 100f) }
        val leanMassKg = fatMassKg?.let { rawEntry.weightKg - it }

        // Fat-Free Mass Index (FFMI)
        val ffmi = leanMassKg?.let { it / heightM.pow(2) }

        // Waist-to-Hip Ratio (WHR)
        val waistHipRatio = if (
            rawEntry.waistCm != null &&
            rawEntry.hipCm != null &&
            rawEntry.hipCm > 0f
        ) rawEntry.waistCm / rawEntry.hipCm
        else null

        // Complex scoring based on overall composition
        val compositionScore = CalculateCompositionScoreUseCase
            .calculate(
                weightKg = rawEntry.weightKg,
                bodyFatPercent = rawEntry.bodyFatPercent,
                muscleMassKg = rawEntry.muscleMassKg,
                visceralFatPercent = rawEntry.visceralFatPercent,
                waistHipRatio = waistHipRatio,
                heightCm = rawEntry.heightCm,
                gender = rawEntry.gender,
                age = rawEntry.age
            )

        val entry = BodyCompositionEntry(
            entryId = 0L, // Handled by Auto-increment in Database
            userId = rawEntry.userId,
            date = rawEntry.date,
            heightCm = rawEntry.heightCm,
            weightKg = rawEntry.weightKg,
            bodyFatPercent = rawEntry.bodyFatPercent,
            waterPercent = rawEntry.waterPercent,
            muscleMassKg = rawEntry.muscleMassKg,
            visceralFatPercent = rawEntry.visceralFatPercent,
            basalCalories = rawEntry.basalCalories,
            metabolicAge = rawEntry.metabolicAge,
            boneMassKg = rawEntry.boneMassKg,
            chestCm = rawEntry.chestCm,
            waistCm = rawEntry.waistCm,
            hipCm = rawEntry.hipCm,
            gluteCm = rawEntry.gluteCm,
            leftArmCm = rawEntry.leftArmCm,
            rightArmCm = rawEntry.rightArmCm,
            leftLegCm = rawEntry.leftLegCm,
            rightLegCm = rawEntry.rightLegCm,
            fatMassKg = fatMassKg,
            leanMassKg = leanMassKg,
            ffmi = ffmi,
            waistHipRatio = waistHipRatio,
            compositionScore = compositionScore
        )

        repository.insertEntry(entry)
    }
}

/**
 * Representational model for raw input data before being processed by the domain layer.
 */
data class RawBodyCompositionInput(
    val userId: Long,
    val date: LocalDate,
    val heightCm: Float,
    val gender: String,
    val age: Int,

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
    val rightLegCm: Float?
)