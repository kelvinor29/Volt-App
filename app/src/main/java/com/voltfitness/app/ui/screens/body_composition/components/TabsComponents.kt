package com.voltfitness.app.ui.screens.body_composition.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.voltfitness.app.ui.screens.body_composition.BodyCompositionDisplayItem
import java.time.format.DateTimeFormatter
import com.voltfitness.app.R

/**
 * Composition tab content displaying all body composition metrics in a grid layout.
 */
@Composable
fun CompositionTab(entry: BodyCompositionDisplayItem?) {
    if (entry == null) {
        EmptyStateMessage(
            message = "No body composition data available.\nAdd your first measurement."
        )
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 50.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Main metrics
        item { SectionTitle(title = "Main Metrics") }
        item { MainMetricsRow(entry) }
        item { AdvancedMetricsRow(entry) }

        // Advanced data
        item {
            Spacer(modifier = Modifier.height(4.dp))
            SectionTitle(title = "Advanced Metrics")
        }
        item { AdvancedDataRow1(entry) }
        item { AdvancedDataRow2(entry) }

        // Calculated indices
        item {
            Spacer(modifier = Modifier.height(4.dp))
            SectionTitle(title = "Calculated Indices")
        }
        item { FatLeanRow(entry) }
        item { IndexRatioRow(entry) }

        item { Spacer(modifier = Modifier.height(16.dp)) } // FAB spacing
    }
}

/**
 * Body measurements tab content organized by body regions.
 */
@Composable
fun MeasurementsTab(entry: BodyCompositionDisplayItem?) {
    if (entry == null) {
        EmptyStateMessage(
            message = "No body measurements recorded.\nAdd your first measurement."
        )
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item { SectionTitle(title = "Torso") }
        item { TorsoRow(entry) }

        item {
            Spacer(modifier = Modifier.height(4.dp))
            SectionTitle(title = "Arms")
        }
        item { ArmsRow(entry) }

        item {
            Spacer(modifier = Modifier.height(4.dp))
            SectionTitle(title = "Legs")
        }
        item { LegsRow(entry) }

        item { Spacer(modifier = Modifier.height(72.dp)) }
    }
}

/**
 * History tab showing timeline of past measurements.
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
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(entries, key = { it.entryId }) { entry ->
            HistoryEntryCard(
                entry = entry,
                onClick = { onEntryClick(entry.entryId) }
            )
        }
        item { Spacer(modifier = Modifier.height(72.dp)) }
    }
}

// ─────────────────────────────────────────────────
// COMPOSITION TAB PRIVATE ROWS
// ─────────────────────────────────────────────────

@Composable
private fun MainMetricsRow(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
            color = Color(0xFFE57373)
        )
    }
}

@Composable
private fun AdvancedMetricsRow(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.FitnessCenter,
            label = "Muscle Mass",
            value = entry.muscleMassKg?.let { "%.1f".format(it) } ?: "—",
            unit = "kg",
            color = Color(0xFF66BB6A)
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Opacity,
            label = "Water",
            value = entry.waterPercent?.let { "%.1f".format(it) } ?: "—",
            unit = "%",
            color = Color(0xFF42A5F5)
        )
    }
}

@Composable
private fun AdvancedDataRow1(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Whatshot,
            label = "Visceral Fat",
            value = entry.visceralFatPercent?.let { "%.1f".format(it) } ?: "—",
            unit = "%",
            color = Color(0xFFFF7043)
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.LocalFireDepartment,
            label = "BMR",
            value = entry.basalCalories?.toString() ?: "—",
            unit = "kcal",
            color = Color(0xFFAB47BC)
        )
    }
}

@Composable
private fun AdvancedDataRow2(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Elderly,
            label = "Metabolic Age",
            value = entry.metabolicAge?.toString() ?: "—",
            unit = "years",
            color = Color(0xFF26A69A)
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.BrokenImage,
            label = "Bone Mass",
            value = entry.boneMassKg?.let { "%.1f".format(it) } ?: "—",
            unit = "kg",
            color = Color(0xFF78909C)
        )
    }
}

@Composable
private fun FatLeanRow(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Balance,
            label = "Fat Mass",
            value = entry.fatMassKg?.let { "%.1f".format(it) } ?: "—",
            unit = "kg",
            color = Color(0xFFEF5350)
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.TrendingUp,
            label = "Lean Mass",
            value = entry.leanMassKg?.let { "%.1f".format(it) } ?: "—",
            unit = "kg",
            color = Color(0xFF43A047)
        )
    }
}

@Composable
private fun IndexRatioRow(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Speed,
            label = "FFMI",
            value = entry.ffmi?.let { "%.1f".format(it) } ?: "—",
            unit = "",
            color = Color(0xFF5C6BC0)
        )
        MetricCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Outlined.Straighten,
            label = "Waist/Hip",
            value = entry.waistHipRatio?.let { "%.2f".format(it) } ?: "—",
            unit = "",
            color = Color(0xFFFF8A65)
        )
    }
}

// ─────────────────────────────────────────────────
// MEASUREMENTS TAB PRIVATE ROWS
// ─────────────────────────────────────────────────

@Composable
private fun TorsoRow(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MeasurementCard(
            modifier = Modifier.weight(1f),
            label = "Chest",
            value = entry.chestCm,
            iconRes = R.drawable.ic_chest
        )
    }
}

@Composable
private fun ArmsRow(entry: BodyCompositionDisplayItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MeasurementCard(
            modifier = Modifier.weight(1f),
            label = "Glutes",
            value = entry.gluteCm,
            iconRes = R.drawable.ic_glutes
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date badge
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = entry.date.dayOfMonth.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = entry.date.format(DateTimeFormatter.ofPattern("MMM")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Main metrics
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "%.1f kg".format(entry.weightKg),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    entry.bodyFatPercent?.let {
                        Text(
                            text = "Fat: %.1f%%".format(it),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    entry.muscleMassKg?.let {
                        Text(
                            text = "Muscle: %.1f kg".format(it),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Score badge
            entry.compositionScore?.let { score ->
                val color = when {
                    score >= 80f -> Color(0xFF4CAF50)
                    score >= 60f -> Color(0xFFFFC107)
                    else -> Color(0xFFF44336)
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = color.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "%.0f".format(score),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
    }
}