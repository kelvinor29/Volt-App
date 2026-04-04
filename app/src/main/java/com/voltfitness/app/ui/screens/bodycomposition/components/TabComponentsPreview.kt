package com.voltfitness.app.ui.screens.bodycomposition.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.voltfitness.app.ui.screens.bodycomposition.BodyCompositionPreviewData
import com.voltfitness.app.ui.theme.VoltTheme

@Preview(showBackground = true, name = "Composition Tab", backgroundColor = 0X252525)
@Composable
private fun CompositionTabPreview() {
    VoltTheme {
        CompositionTab(entry = BodyCompositionPreviewData.sampleEntry)
    }
}

@Preview(showBackground = true, name = "Measurements Tab", backgroundColor = 0X252525)
@Composable
private fun MeasurementsTabPreview() {
    VoltTheme {
        MeasurementsTab(entry = BodyCompositionPreviewData.sampleEntry)
    }
}

@Preview(showBackground = true, name = "History Tab", backgroundColor = 0X252525)
@Composable
private fun HistoryTabPreview() {
    VoltTheme {
        HistoryTab(
            entries = BodyCompositionPreviewData.sampleHistory,
            onEntryClick = {}
        )
    }
}
