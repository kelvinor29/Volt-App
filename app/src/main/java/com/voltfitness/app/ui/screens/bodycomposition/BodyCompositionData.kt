package com.voltfitness.app.ui.screens.bodycomposition


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

}
