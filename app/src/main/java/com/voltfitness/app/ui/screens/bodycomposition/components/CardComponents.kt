package com.voltfitness.app.ui.screens.bodycomposition.components

import com.voltfitness.app.R
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.designsystem.component.voltSectionShape
import com.voltfitness.app.ui.theme.VoltIconSize
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * Visual card for health metrics (e.g., Body Fat, Water %).
 *
 * Uses a dynamic tinting strategy where the [color] parameter drives
 * both the icon tint and a 8% alpha container background.
 *
 * @param icon Symbol representing the specific health metric.
 * @param label Human-readable title of the metric.
 * @param value Formatted numeric string.
 * @param unit Measurement unit (kg, %, kcal).
 * @param color Theme color used for emphasis and background tinting.
 */
@Composable
fun MetricCard(
    icon: ImageVector,
    label: String,
    value: String,
    unit: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = voltSectionShape,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.08f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(VoltSpacing.medium)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(VoltIconSize.small)
                )
                Spacer(modifier = Modifier.width(VoltSpacing.small))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(VoltSpacing.small))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (unit.isNotEmpty()) {
                    Text(
                        text = " $unit",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = VoltSpacing.extraSmall)
                    )
                }
            }
        }
    }
}

/**
 * Specialized card for anthropometric circumferences (e.g., Waist, Chest).
 *
 * Includes an illustration/icon placeholder and handles null states by
 * displaying a placeholder dash ("—").
 *
 * @param label Anatomical location name.
 * @param value Circumference value in centimeters.
 * @param iconRes Drawable resource representing the measurement area.
 */
@Composable
fun MeasurementCard(
    label: String,
    value: Float?,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = voltSectionShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(VoltSpacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier
                    .size(VoltIconSize.extraLarge)
                    .alpha(0.8f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(VoltSpacing.small))

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(VoltSpacing.extraSmall))

            Text(
                text = value?.let { "%.1f cm".format(it) } ?: "—",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview()
@Composable
private fun MetricCardPreview() {
    VoltTheme {
        MetricCard(
            icon = Icons.Outlined.MonitorWeight,
            label = "Weight",
            value = "72.5",
            unit = "kg",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview()
@Composable
private fun MetricCardRowPreview() {
    VoltTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.MonitorWeight,
                label = "Weight",
                value = "72.5",
                unit = "kg",
                color = MaterialTheme.colorScheme.primary
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.WaterDrop,
                label = "Body Fat",
                value = "18.2",
                unit = "%",
                color = Color(0xFFE57373)
            )
        }
    }
}

@Preview()
@Composable
private fun MetricCardLongContentPreview() {
    VoltTheme {
        MetricCard(
            icon = Icons.Outlined.FitnessCenter,
            label = "Muscle Mass Very Long Label",
            value = "123.456",
            unit = "kg",
            color = Color(0xFF66BB6A),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview()
@Composable
private fun MetricCardNoUnitPreview() {
    VoltTheme {
        MetricCard(
            icon = Icons.Outlined.Speed,
            label = "FFMI",
            value = "21.3",
            unit = "",
            color = Color(0xFF5C6BC0),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview()
@Composable
private fun MeasurementCardPreview() {
    VoltTheme {
        MeasurementCard(
            label = "Chest",
            value = 102.5f,
            iconRes = R.drawable.ic_chest,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview()
@Composable
private fun MeasurementCardEmptyPreview() {
    VoltTheme {
        MeasurementCard(
            label = "Waist",
            value = null,
            iconRes = R.drawable.ic_hips,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview()
@Composable
private fun MeasurementRowPreview() {
    VoltTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MeasurementCard(
                modifier = Modifier.weight(1f),
                label = "Left Arm",
                value = 32.4f,
                iconRes = R.drawable.ic_arms
            )
            MeasurementCard(
                modifier = Modifier.weight(1f),
                label = "Right Arm",
                value = 31.8f,
                iconRes = R.drawable.ic_arms
            )
        }
    }
}

@Preview(heightDp = 300)
@Composable
private fun MetricsGridPreview() {
    VoltTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.MonitorWeight,
                    label = "Weight",
                    value = "72.5",
                    unit = "kg",
                    color = MaterialTheme.colorScheme.primary
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.WaterDrop,
                    label = "Body Fat",
                    value = "18.2",
                    unit = "%",
                    color = Color(0xFFE57373)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.FitnessCenter,
                    label = "Muscle",
                    value = "34.1",
                    unit = "kg",
                    color = Color(0xFF66BB6A)
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Outlined.Opacity,
                    label = "Water",
                    value = "55.3",
                    unit = "%",
                    color = Color(0xFF42A5F5)
                )
            }
        }
    }
}