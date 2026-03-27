package com.voltfitness.app.ui.screens.body_composition.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.voltfitness.app.ui.screens.body_composition.BodyCompositionPreviewData
import com.voltfitness.app.ui.theme.VoltTheme

@Preview(showBackground = true, name = "Quick Summary Header")
@Composable
private fun QuickSummaryHeaderPreview() {
    val entry = BodyCompositionPreviewData.sampleEntry
    VoltTheme {
        QuickSummaryHeader(
            userName = "John Doe",
            weight = entry.weightKg,
            bodyFat = entry.bodyFatPercent,
            muscleMass = entry.muscleMassKg,
            score = entry.compositionScore,
            lastDate = entry.date
        )
    }
}
