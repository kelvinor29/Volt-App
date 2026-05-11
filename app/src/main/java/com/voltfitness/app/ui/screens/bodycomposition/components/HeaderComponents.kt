package com.voltfitness.app.ui.screens.bodycomposition.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.R
import com.voltfitness.app.core.designsystem.component.voltSectionShape
import com.voltfitness.app.ui.theme.VoltError
import com.voltfitness.app.ui.theme.VoltIconSize
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltSuccess
import com.voltfitness.app.ui.theme.VoltTheme
import com.voltfitness.app.ui.theme.VoltWarning
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val SummaryDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

/**
 * Featured header card for the body composition dashboard.
 *
 * Displays core metrics (Weight, Fat, Muscle) and the overall composition score.
 */
@Composable
fun QuickSummaryHeader(
    userName: String,
    weight: Float,
    bodyFat: Float?,
    muscleMass: Float?,
    score: Float?,
    lastDate: LocalDate,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(VoltSpacing.medium),
        shape = voltSectionShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(VoltSpacing.medium)
        ) {
            HeaderTopRow(userName, lastDate, score)

            Spacer(modifier = Modifier.height(VoltSpacing.medium))

            MetricsRow(weight, bodyFat, muscleMass)
        }
    }
}

/**
 * Internal layout for the header's identity and score indicator.
 */
@Composable
private fun HeaderTopRow(
    userName: String,
    lastDate: LocalDate,
    score: Float?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = userName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(
                    R.string.latest_measurement,
                    lastDate.format(SummaryDateFormatter)
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        score?.let { ScoreIndicator(score = it) }
    }
}

/**
 * Internal layout for the core metric items.
 */
@Composable
private fun MetricsRow(
    weight: Float,
    bodyFat: Float?,
    muscleMass: Float?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickStatItem(
            icon = Icons.Outlined.MonitorWeight,
            value = "%.1f".format(weight),
            unit = "kg",
            label = stringResource(R.string.weight)
        )
        bodyFat?.let {
            QuickStatItem(
                icon = Icons.Outlined.WaterDrop,
                value = "%.1f".format(it),
                unit = "%",
                label = stringResource(R.string.fat)
            )
        }
        muscleMass?.let {
            QuickStatItem(
                icon = Icons.Outlined.FitnessCenter,
                value = "%.1f".format(it),
                unit = "kg",
                label = stringResource(R.string.muscle)
            )
        }
    }
}

/**
 * Color-coded circular score indicator.
 */
@Composable
private fun ScoreIndicator(score: Float) {
    val color = when {
        score >= 80f -> VoltSuccess
        score >= 60f -> VoltWarning
        else -> VoltError
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f))
    ) {
        Text(
            text = "%.0f".format(score),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
    }
}

/**
 * Compact metric display item for header summaries.
 */
@Composable
private fun QuickStatItem(
    icon: ImageVector,
    value: String,
    unit: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(VoltIconSize.small)
        )
        Spacer(modifier = Modifier.height(VoltSpacing.extraSmall))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// region Previews

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun QuickSummaryHeaderPreview() {
    VoltTheme {
        QuickSummaryHeader(
            userName = "Kelvin",
            weight = 82.5f,
            bodyFat = 18.2f,
            muscleMass = 38.4f,
            score = 88f,
            lastDate = LocalDate.now()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun QuickSummaryHeaderIncompletePreview() {
    VoltTheme {
        QuickSummaryHeader(
            userName = "New User",
            weight = 70.0f,
            bodyFat = null,
            muscleMass = null,
            score = null,
            lastDate = LocalDate.now()
        )
    }
}
// endregion