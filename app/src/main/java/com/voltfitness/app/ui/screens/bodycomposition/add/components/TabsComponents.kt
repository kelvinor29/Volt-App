package com.voltfitness.app.ui.screens.bodycomposition.add.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.CenterFocusStrong
import androidx.compose.material.icons.outlined.Elderly
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Height
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.SportsGymnastics
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material.icons.outlined.WidthNormal
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.designsystem.component.VoltMeasurementField
import com.voltfitness.app.ui.screens.bodycomposition.add.AddBodyCompositionEvent
import com.voltfitness.app.ui.screens.bodycomposition.add.AddBodyCompositionUiState

/**
 * Tab content for scale-based body composition metrics.
 * Stateless — receives state and dispatches events upward.
 */
@Composable
fun BodyCompositionTab(
    uiState: AddBodyCompositionUiState,
    onEvent: (AddBodyCompositionEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        MeasurementPairRow {
            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.heightCm,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateHeight(it)) },
                label = "Height *",
                suffix = "cm",
                icon = Icons.Outlined.Height,
                isError = uiState.heightCm.isNotBlank() && !uiState.isHeightValid,
            )

            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.weightKg,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateWeight(it)) },
                label = "Weight *",
                suffix = "kg",
                icon = Icons.Outlined.MonitorWeight,
                isError = uiState.weightKg.isNotBlank() && !uiState.isWeightValid,
            )
        }


        MeasurementPairRow {
            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.bodyFatPercent,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateBodyFat(it)) },
                label = "Body Fat",
                suffix = "%",
                icon = Icons.Outlined.WaterDrop,
            )
            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.waterPercent,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateWater(it)) },
                label = "Water",
                suffix = "%",
                icon = Icons.Outlined.Opacity,
            )
        }

        VoltMeasurementField(
            value = uiState.muscleMassKg,
            onValueChange = { onEvent(AddBodyCompositionEvent.UpdateMuscleMass(it)) },
            label = "Muscle Mass",
            suffix = "kg",
            icon = Icons.Outlined.FitnessCenter,
        )

        MeasurementPairRow {
            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.basalCalories,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateBasalCalories(it)) },
                label = "Basal Calories",
                suffix = "kcal",
                icon = Icons.Outlined.LocalFireDepartment,
            )
            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.metabolicAge,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateMetabolicAge(it)) },
                label = "Metabolic Age",
                suffix = "yrs",
                icon = Icons.Outlined.Elderly,
            )
        }

        MeasurementPairRow {
            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.boneMassKg,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateBoneMass(it)) },
                label = "Bone Mass",
                suffix = "kg",
                icon = Icons.Outlined.BrokenImage,
            )
            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.visceralFatPercent,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateVisceralFat(it)) },
                label = "Visceral Fat",
                suffix = "%",
                icon = Icons.Outlined.Whatshot,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


/**
 * Tab content for tape-based body measurements.
 * Stateless — receives state and dispatches events upward.
 */
@Composable
fun BodyMeasurementsTab(
    uiState: AddBodyCompositionUiState,
    onEvent: (AddBodyCompositionEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // --- Torso Measurements ---
        VoltMeasurementField(
            value = uiState.chestCm,
            onValueChange = { onEvent(AddBodyCompositionEvent.UpdateChest(it)) },
            label = "Chest",
            suffix = "cm",
            icon = Icons.Outlined.Accessibility
        )

        VoltMeasurementField(
            value = uiState.waistCm,
            onValueChange = { onEvent(AddBodyCompositionEvent.UpdateWaist(it)) },
            label = "Waist",
            suffix = "cm",
            icon = Icons.Outlined.CenterFocusStrong
        )

        MeasurementPairRow {
            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.hipCm,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateHip(it)) },
                label = "Hips",
                suffix = "cm",
                icon = Icons.Outlined.WidthNormal
            )
            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.gluteCm,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateGlute(it)) },
                label = "Glutes",
                suffix = "cm",
                icon = Icons.Outlined.SportsGymnastics
            )
        }

        // ── Paired: Arms ──
        MeasurementPairRow {
            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.leftArmCm,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateLeftArm(it)) },
                label = "Left Arm",
                suffix = "cm",
                icon = Icons.Outlined.FitnessCenter,
            )
            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.rightArmCm,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateRightArm(it)) },
                label = "Right Arm",
                suffix = "cm",
                icon = Icons.Outlined.FitnessCenter,
            )
        }

        // ── Paired: Legs ──
        MeasurementPairRow {
            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.leftLegCm,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateLeftLeg(it)) },
                label = "Left Leg",
                suffix = "cm",
                icon = Icons.AutoMirrored.Outlined.DirectionsRun,
            )
            VoltMeasurementField(
                modifier = Modifier.weight(1f),
                value = uiState.rightLegCm,
                onValueChange = { onEvent(AddBodyCompositionEvent.UpdateRightLeg(it)) },
                label = "Right Leg",
                suffix = "cm",
                icon = Icons.AutoMirrored.Outlined.DirectionsRun,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MeasurementPairRow(
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        content = content
    )
}
