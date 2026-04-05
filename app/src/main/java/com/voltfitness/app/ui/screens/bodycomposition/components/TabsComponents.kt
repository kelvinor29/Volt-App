package com.voltfitness.app.ui.screens.bodycomposition.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.Elderly
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.voltfitness.app.R
import com.voltfitness.app.core.designsystem.component.voltFieldShape
import com.voltfitness.app.core.designsystem.component.voltSectionShape
import com.voltfitness.app.ui.screens.bodycomposition.BodyCompositionDisplayItem
import com.voltfitness.app.ui.theme.VoltAccentBlue
import com.voltfitness.app.ui.theme.VoltAccentOrange
import com.voltfitness.app.ui.theme.VoltAccentPurple
import com.voltfitness.app.ui.theme.VoltError
import com.voltfitness.app.ui.theme.VoltErrorDim
import com.voltfitness.app.ui.theme.VoltInfo
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltSuccess
import com.voltfitness.app.ui.theme.VoltSuccessDim
import com.voltfitness.app.ui.theme.VoltTextTertiary
import com.voltfitness.app.ui.theme.VoltWarning
import java.time.format.DateTimeFormatter

/**
 * Detailed composition tab displaying metrics like body fat, water, and muscle mass.
 */
@Composable
fun CompositionTab(entry: BodyCompositionDisplayItem?) {
    if (entry == null) {
        EmptyStateMessage(message = "No data available. Add your first measurement.")
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(
            top = VoltSpacing.medium,
            start = VoltSpacing.medium,
            end = VoltSpacing.medium,
            bottom = VoltSpacing.xhuge
        ),
        verticalArrangement = Arrangement.spacedBy(VoltSpacing.medium),
        modifier = Modifier.fillMaxSize()
    ) {
        item { SectionTitle(title = "Primary Metrics") }
        item { MainMetricsRow(entry) }

        item {
            Spacer(modifier = Modifier.height(VoltSpacing.extraSmall))
            SectionTitle(title = "Advanced Composition")
        }
        item { AdvancedMetricsRow(entry) }
        item { AdvancedDataRow1(entry) }
        item { AdvancedDataRow2(entry) }

        item {
            Spacer(modifier = Modifier.height(VoltSpacing.extraSmall))
            SectionTitle(title = "Calculated Indices")
        }
        item { FatLeanRow(entry) }
        item { IndexRatioRow(entry) }
    }
}

/**
 * Body measurements tab content organized by anatomical regions.
 */
@Composable
fun MeasurementsTab(entry: BodyCompositionDisplayItem?) {
    if (entry == null) {
        EmptyStateMessage(message = "No body measurements recorded.")
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(
            top = VoltSpacing.medium,
            start = VoltSpacing.medium,
            end = VoltSpacing.medium,
            bottom = VoltSpacing.xhuge
        ),
        verticalArrangement = Arrangement.spacedBy(VoltSpacing.medium),
        modifier = Modifier.fillMaxSize()
    ) {
        item { SectionTitle(title = "Torso") }
        item { TorsoRow(entry) }

        item {
            Spacer(modifier = Modifier.height(VoltSpacing.extraSmall))
            SectionTitle(title = "Arms")
        }
        item { ArmsRow(entry) }

        item {
            Spacer(modifier = Modifier.height(VoltSpacing.extraSmall))
            SectionTitle(title = "Legs")
        }
        item { LegsRow(entry) }
    }
}

/**
 * Timeline of historical measurements.
 */
@Composable
fun HistoryTab(
    entries: List<BodyCompositionDisplayItem>,
    onEntryClick: (Long) -> Unit
) {
    if (entries.isEmpty()) {
        EmptyStateMessage(message = "No measurement history available.")
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(
            top = VoltSpacing.medium,
            start = VoltSpacing.medium,
            end = VoltSpacing.medium,
            bottom = VoltSpacing.xhuge
        ),
        verticalArrangement = Arrangement.spacedBy(VoltSpacing.small),
        modifier = Modifier.fillMaxSize()
    ) {
        items(entries, key = { it.entryId }) { entry ->
            HistoryEntryCard(
                entry = entry,
                onClick = { onEntryClick(entry.entryId) }
            )
        }
    }
}

// ─────────────────────────────────────────────────
// COMPOSITION TAB PRIVATE ROWS
// ─────────────────────────────────────────────────

@Composable
private fun MainMetricsRow(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(VoltSpacing.medium)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.MonitorWeight,
            label = "Weight",
            value = "%.1f".format(entry.weightKg),
            unit = "kg",
            color = MaterialTheme.colorScheme.primary
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.WaterDrop,
            label = "Body Fat",
            value = entry.bodyFatPercent?.let { "%.1f".format(it) } ?: "—",
            unit = "%",
            color = VoltError
        )
    }
}

@Composable
private fun AdvancedMetricsRow(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(VoltSpacing.medium)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.FitnessCenter,
            label = "Muscle Mass",
            value = entry.muscleMassKg?.let { "%.1f".format(it) } ?: "—",
            unit = "kg",
            color = VoltSuccess
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Opacity,
            label = "Water",
            value = entry.waterPercent?.let { "%.1f".format(it) } ?: "—",
            unit = "%",
            color = VoltInfo
        )
    }
}

@Composable
private fun AdvancedDataRow1(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(VoltSpacing.medium)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Whatshot,
            label = "Visceral Fat",
            value = entry.visceralFatPercent?.let { "%.1f".format(it) } ?: "—",
            unit = "%",
            color = VoltAccentOrange
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.LocalFireDepartment,
            label = "BMR",
            value = entry.basalCalories?.toString() ?: "—",
            unit = "kcal",
            color = VoltAccentPurple
        )
    }
}

@Composable
private fun AdvancedDataRow2(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(VoltSpacing.medium)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Elderly,
            label = "Metabolic Age",
            value = entry.metabolicAge?.toString() ?: "—",
            unit = "years",
            color = VoltSuccessDim
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.BrokenImage,
            label = "Bone Mass",
            value = entry.boneMassKg?.let { "%.1f".format(it) } ?: "—",
            unit = "kg",
            color = VoltTextTertiary
        )
    }
}

@Composable
private fun FatLeanRow(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(VoltSpacing.medium)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Balance,
            label = "Fat Mass",
            value = entry.fatMassKg?.let { "%.1f".format(it) } ?: "—",
            unit = "kg",
            color = VoltErrorDim
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.AutoMirrored.Outlined.TrendingUp,
            label = "Lean Mass",
            value = entry.leanMassKg?.let { "%.1f".format(it) } ?: "—",
            unit = "kg",
            color = VoltSuccessDim
        )
    }
}

@Composable
private fun IndexRatioRow(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(VoltSpacing.medium)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Speed,
            label = "FFMI",
            value = entry.ffmi?.let { "%.1f".format(it) } ?: "—",
            unit = "",
            color = VoltAccentBlue
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Straighten,
            label = "Waist/Hip",
            value = entry.waistHipRatio?.let { "%.2f".format(it) } ?: "—",
            unit = "",
            color = VoltWarning
        )
    }
}

// ─────────────────────────────────────────────────
// MEASUREMENTS TAB PRIVATE ROWS
// ─────────────────────────────────────────────────

@Composable
private fun TorsoRow(entry: BodyCompositionDisplayItem) {
    MeasurementCard(
        modifier = Modifier.fillMaxWidth(),
        label = "Chest",
        value = entry.chestCm,
        iconRes = R.drawable.ic_chest
    )
    Spacer(Modifier.height(VoltSpacing.medium))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(VoltSpacing.medium)
    ) {
        MeasurementCard(
            modifier = Modifier.weight(1f),
            label = "Waist",
            value = entry.waistCm,
            iconRes = R.drawable.ic_hips
        )
        MeasurementCard(
            modifier = Modifier.weight(1f),
            label = "Hip",
            value = entry.hipCm,
            iconRes = R.drawable.ic_hips
        )
    }
}

@Composable
private fun ArmsRow(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(VoltSpacing.medium)
    ) {
        MeasurementCard(
            modifier = Modifier.weight(1f),
            label = "Left Arm",
            value = entry.leftArmCm,
            iconRes = R.drawable.ic_arms
        )
        MeasurementCard(
            modifier = Modifier.weight(1f),
            label = "Right Arm",
            value = entry.rightArmCm,
            iconRes = R.drawable.ic_arms
        )
    }
}

@Composable
private fun LegsRow(entry: BodyCompositionDisplayItem) {
    MeasurementCard(
        modifier = Modifier.fillMaxWidth(),
        label = "Glutes",
        value = entry.gluteCm,
        iconRes = R.drawable.ic_glutes
    )
    Spacer(Modifier.height(VoltSpacing.medium))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(VoltSpacing.medium),
    ) {
        MeasurementCard(
            modifier = Modifier.weight(1f),
            label = "Left Leg",
            value = entry.leftLegCm,
            iconRes = R.drawable.ic_legs
        )
        MeasurementCard(
            modifier = Modifier.weight(1f),
            label = "Right Leg",
            value = entry.rightLegCm,
            iconRes = R.drawable.ic_legs
        )
    }
}

// ─────────────────────────────────────────────────
// HISTORY TAB PRIVATE COMPONENT
// ─────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryEntryCard(
    entry: BodyCompositionDisplayItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = voltSectionShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(VoltSpacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date badge
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(voltFieldShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(horizontal = VoltSpacing.medium, vertical = VoltSpacing.small)
            ) {
                Text(
                    text = entry.date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = entry.date.format(DateTimeFormatter.ofPattern("MMM")),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(VoltSpacing.medium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "%.1f kg".format(entry.weightKg),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(VoltSpacing.medium)) {
                    entry.bodyFatPercent?.let {
                        Text(
                            text = "Fat: %.1f%%".format(it),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    entry.muscleMassKg?.let {
                        Text(
                            text = "Muscle: %.1f kg".format(it),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}