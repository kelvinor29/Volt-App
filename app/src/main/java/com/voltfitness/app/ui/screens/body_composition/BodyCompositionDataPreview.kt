package com.voltfitness.app.ui.screens.body_composition

import java.time.LocalDate

/**
 * Fake data providers for previews of BodyComposition UI.
 */
object BodyCompositionPreviewData {

    val sampleEntry = BodyCompositionDisplayItem(
        entryId = 1L,
        date = LocalDate.now().minusDays(1),
        weightKg = 82.5f,
        bodyFatPercent = 17.8f,
        waterPercent = 58.2f,
        muscleMassKg = 38.4f,
        visceralFatPercent = 9.5f,
        basalCalories = 1850,
        metabolicAge = 28,
        boneMassKg = 3.2f,
        chestCm = 102f,
        waistCm = 82f,
        hipCm = 98f,
        gluteCm = 100f,
        leftArmCm = 35f,
        rightArmCm = 36f,
        leftLegCm = 57f,
        rightLegCm = 58f,
        fatMassKg = 14.7f,
        leanMassKg = 67.8f,
        ffmi = 22.4f,
        waistHipRatio = 0.84f,
        compositionScore = 88f
    )

    val sampleHistory = listOf(
        sampleEntry,
        sampleEntry.copy(
            entryId = 2L,
            date = LocalDate.now().minusDays(7),
            weightKg = 83.0f,
            bodyFatPercent = 18.3f,
            compositionScore = 84f
        ),
        sampleEntry.copy(
            entryId = 3L,
            date = LocalDate.now().minusDays(14),
            weightKg = 84.2f,
            bodyFatPercent = 19.0f,
            compositionScore = 80f
        )
    )

    val sampleState = BodyCompositionUiState(
        isLoading = false,
        userName = "John Doe",
        gender = "male",
        goal = "Hypertrophy",
        latestEntry = sampleEntry,
        historyEntries = sampleHistory,
    )
}
