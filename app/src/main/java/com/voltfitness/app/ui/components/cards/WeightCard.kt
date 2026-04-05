package com.voltfitness.app.ui.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.designsystem.component.voltFieldShape
import com.voltfitness.app.core.designsystem.component.voltSectionShape
import com.voltfitness.app.domain.model.ProgressStatus
import com.voltfitness.app.ui.components.VoltCard
import com.voltfitness.app.ui.screens.home.HomeUiState
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * A dashboard molecule that visualizes the user's weight trends and progress.
 *
 * Uses semantic coloring based on [ProgressStatus]:
 * - POSITIVE: Primary/Success colors (Weight loss usually).
 * - NEGATIVE: Error colors (Weight gain/Unwanted trend).
 * - NEUTRAL: Surface variant colors.
 *
 * @param uiState Snapshot of the home screen data including weight and progress text.
 * @param onClick Optional navigation or detail view trigger.
 * @param modifier Layout modifiers for the card container.
 */
@Composable
fun WeightCard(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val resources = getWeightProgressResources(uiState.progressStatus)

    VoltCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .padding(VoltSpacing.medium)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Section: Current Weight Information
            WeightInfoSection(
                weight = uiState.currentWeight,
                unit = uiState.weightUnit,
                lastUpdated = uiState.lastWeightUpdated,
                modifier = Modifier.weight(1f)
            )

            // Right Section: Progress Indicators
            ProgressSection(
                primaryText = uiState.primaryChangeText,
                secondaryText = uiState.secondaryChangeText,
                resources = resources
            )
        }
    }
}

@Composable
private fun WeightInfoSection(
    weight: String,
    unit: String,
    lastUpdated: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Current Weight",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = weight.ifEmpty { "--.-" },
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = " $unit",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = VoltSpacing.extraSmall),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (lastUpdated.isNotEmpty()) {
            Text(
                text = lastUpdated,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun ProgressSection(
    primaryText: String,
    secondaryText: String?,
    resources: ProgressResources
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Surface(
            color = resources.containerColor,
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = VoltSpacing.small, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                resources.icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = resources.contentColor
                    )
                }
                Text(
                    text = primaryText,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = resources.contentColor
                )
            }
        }

        secondaryText?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Internal state holder for progress-related visual assets.
 */
private data class ProgressResources(
    val contentColor: Color,
    val containerColor: Color,
    val icon: ImageVector?
)

@Composable
private fun getWeightProgressResources(status: ProgressStatus): ProgressResources {
    return when (status) {
        ProgressStatus.POSITIVE -> ProgressResources(
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            icon = Icons.Default.KeyboardArrowUp
        )
        ProgressStatus.NEGATIVE -> ProgressResources(
            contentColor = MaterialTheme.colorScheme.error,
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
            icon = Icons.Default.KeyboardArrowDown
        )
        ProgressStatus.NEUTRAL -> ProgressResources(
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            icon = null
        )
    }
}

// region Previews maintained for UI validation...

// region Previews

@Preview(name = "Weight Loss (Positive)", showBackground = true)
@Composable
private fun WeightCardPositivePreview() {
    VoltTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            WeightCard(
                uiState = HomeUiState(
                    currentWeight = "75.5",
                    weightUnit = "kg",
                    progressStatus = ProgressStatus.POSITIVE,
                    primaryChangeText = "-0.5 kg",
                    secondaryChangeText = "Nice job!",
                    lastWeightUpdated = "Feb 11, 2026"
                )
            )
        }
    }
}

@Preview(name = "Weight Increase (Negative)", showBackground = true)
@Composable
private fun WeightCardNegativePreview() {
    VoltTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            WeightCard(
                uiState = HomeUiState(
                    currentWeight = "82.3",
                    weightUnit = "kg",
                    progressStatus = ProgressStatus.NEGATIVE,
                    primaryChangeText = "+1.2 kg",
                    secondaryChangeText = "Keep pushing",
                    lastWeightUpdated = "Yesterday"
                )
            )
        }
    }
}

// endregion