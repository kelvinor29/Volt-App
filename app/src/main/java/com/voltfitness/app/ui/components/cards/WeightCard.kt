import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.domain.model.ProgressStatus
import com.voltfitness.app.ui.components.VoltCard
import com.voltfitness.app.ui.screens.home.HomeUiState
import com.voltfitness.app.ui.theme.VoltSpacing
import androidx.compose.material.icons.Icons
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * A card molecule that displays the user's current weight and progress.
 * Refactored to use [HomeUiState] while maintaining the premium design.
 */
@Composable
fun WeightCard(
    uiState: HomeUiState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val (contentColor, icon) = when (uiState.progressStatus) {
        ProgressStatus.POSITIVE -> MaterialTheme.colorScheme.primary to Icons.Default.KeyboardArrowUp
        ProgressStatus.NEGATIVE -> MaterialTheme.colorScheme.error to Icons.Default.KeyboardArrowDown
        ProgressStatus.NEUTRAL -> MaterialTheme.colorScheme.onSurfaceVariant to null
    }

    val containerColor = when (uiState.progressStatus) {
        ProgressStatus.POSITIVE -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ProgressStatus.NEGATIVE -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
        ProgressStatus.NEUTRAL -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    }

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
            // Left Section: Current Weight
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Current Weight",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = uiState.currentWeight.ifEmpty { "--.-" },
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = " ${uiState.weightUnit}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 6.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (uiState.lastWeightUpdated.isNotEmpty()) {
                    Text(
                        text = uiState.lastWeightUpdated,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            // Right Section: Progress Indicator
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Surface(
                    color = containerColor,
                    shape = MaterialTheme.shapes.extraSmall,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = contentColor
                            )
                        }
                        Text(
                            text = uiState.primaryChangeText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = contentColor
                        )
                    }
                }

                uiState.secondaryChangeText?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

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