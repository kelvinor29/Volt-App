package com.voltfitness.app.data.mappers

import com.voltfitness.app.data.local.entities.BodyCompositionEntity
import com.voltfitness.app.domain.model.BodyCompositionEntry
import java.time.LocalDate

fun BodyCompositionEntity.toDomain(): BodyCompositionEntry =
    BodyCompositionEntry(
        entryId = entryId,
        userId = userId,
        date = LocalDate.ofEpochDay(dateEpochDay),
        heightCm = heightCm,
        weightKg = weightKg,
        bodyFatPercent = bodyFatPercent,
        waterPercent = waterPercent,
        muscleMassKg = muscleMassKg,
        visceralFatPercent = visceralFatPercent,
        basalCalories = basalCalories,
        metabolicAge = metabolicAge,
        boneMassKg = boneMassKg,
        chestCm = chestCm,
        waistCm = waistCm,
        hipCm = hipCm,
        gluteCm = gluteCm,
        leftArmCm = leftArmCm,
        rightArmCm = rightArmCm,
        leftLegCm = leftLegCm,
        rightLegCm = rightLegCm,
        fatMassKg = fatMassKg,
        leanMassKg = leanMassKg,
        ffmi = ffmi,
        waistHipRatio = waistHipRatio,
        compositionScore = compositionScore
    )

fun BodyCompositionEntry.toEntity(): BodyCompositionEntity =
    BodyCompositionEntity(
        entryId = entryId,
        userId = userId,
        dateEpochDay = date.toEpochDay(),
        heightCm = heightCm,
        weightKg = weightKg,
        bodyFatPercent = bodyFatPercent,
        waterPercent = waterPercent,
        muscleMassKg = muscleMassKg,
        visceralFatPercent = visceralFatPercent,
        basalCalories = basalCalories,
        metabolicAge = metabolicAge,
        boneMassKg = boneMassKg,
        chestCm = chestCm,
        waistCm = waistCm,
        hipCm = hipCm,
        gluteCm = gluteCm,
        leftArmCm = leftArmCm,
        rightArmCm = rightArmCm,
        leftLegCm = leftLegCm,
        rightLegCm = rightLegCm,
        fatMassKg = fatMassKg,
        leanMassKg = leanMassKg,
        ffmi = ffmi,
        waistHipRatio = waistHipRatio,
        compositionScore = compositionScore,
        createdAt = System.currentTimeMillis()
    )