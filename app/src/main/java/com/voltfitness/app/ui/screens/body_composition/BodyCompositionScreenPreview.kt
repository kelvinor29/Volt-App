package com.voltfitness.app.ui.screens.body_composition

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.voltfitness.app.ui.theme.VoltTheme
import java.time.LocalDate

/**
 * Preview data shared across all BodyCompositionScreen previews.
 */
private val previewEntry = BodyCompositionDisplayItem(
    entryId = 1L,
    date = LocalDate.of(2026, 3, 8),
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

private val previewHistory = listOf(
    previewEntry,
    previewEntry.copy(
        entryId = 2L,
        date = LocalDate.of(2026, 3, 1),
        weightKg = 83.0f,
        bodyFatPercent = 18.3f,
        compositionScore = 84f
    ),
    previewEntry.copy(
        entryId = 3L,
        date = LocalDate.of(2026, 2, 22),
        weightKg = 84.2f,
        bodyFatPercent = 19.0f,
        compositionScore = 80f
    )
)

private val previewState = BodyCompositionUiState(
    isLoading = false,
    userName = "John Doe",
    gender = "Male",
    goal = "Hypertrophy",
    latestEntry = previewEntry,
    historyEntries = previewHistory
)

/**
 * Default preview — shows the screen with data loaded.
 * The HorizontalPager starts at page 0 (Composition tab) by default.
 */
@Preview(showBackground = true, name = "Body Composition – With Data")
@Composable
private fun BodyCompositionScreenPreview() {
    VoltTheme {
        BodyCompositionScreen(
            uiState = previewState,
            onAddNewMeasurement = {},
            onEntryClick = {},
            modifier = Modifier
        )
    }
}

/**
 * Loading state preview — shows centered progress indicator.
 */
@Preview(showBackground = true, name = "Body Composition – Loading")
@Composable
private fun BodyCompositionScreenLoadingPreview() {
    VoltTheme {
        BodyCompositionScreen(
            uiState = BodyCompositionUiState(isLoading = true),
            onAddNewMeasurement = {},
            onEntryClick = {},
            modifier = Modifier
        )
    }
}

/**
 * Empty state preview — no entries available.
 */
@Preview(showBackground = true, name = "Body Composition – Empty")
@Composable
private fun BodyCompositionScreenEmptyPreview() {
    VoltTheme {
        BodyCompositionScreen(
            uiState = BodyCompositionUiState(
                isLoading = false,
                userName = "John Doe",
                latestEntry = null,
                historyEntries = emptyList()
            ),
            onAddNewMeasurement = {},
            onEntryClick = {},
            modifier = Modifier
        )
    }
}
