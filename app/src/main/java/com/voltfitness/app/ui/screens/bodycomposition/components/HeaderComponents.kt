package com.voltfitness.app.ui.screens.bodycomposition.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Quick summary header card displaying user info, latest measurement stats, and composition score.
 *
 * @param userName Display name of the user
 * @param weight Latest body weight in kg
 * @param bodyFat Latest body fat percentage
 * @param muscleMass Latest muscle mass in kg
 * @param score Composition score (40-120 scale)
 * @param lastDate Date of the latest measurement
 */
@Composable
fun QuickSummaryHeader(
    userName: String,
    weight: Float,
    bodyFat: Float?,
    muscleMass: Float?,
    score: Float?,
    lastDate: LocalDate
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
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
                        text = "Latest measurement: ${lastDate.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                // Score circular indicator
                score?.let {
                    ScoreIndicator(score = it)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickStatItem(
                    icon = Icons.Outlined.MonitorWeight,
                    value = "%.1f".format(weight),
                    unit = "kg",
                    label = "Weight"
                )
                bodyFat?.let {
                    QuickStatItem(
                        icon = Icons.Outlined.WaterDrop,
                        value = "%.1f".format(it),
                        unit = "%",
                        label = "Fat"
                    )
                }
                muscleMass?.let {
                    QuickStatItem(
                        icon = Icons.Outlined.FitnessCenter,
                        value = "%.1f".format(it),
                        unit = "kg",
                        label = "Muscle"
                    )
                }
            }
        }
    }
}

/**
 * Circular score indicator with color-coded status.
 *
 * @param score Raw score value (40-120)
 */
@Composable
private fun ScoreIndicator(score: Float) {
    val color = when {
        score >= 80f -> Color(0xFF4CAF50) // Green - Excellent
        score >= 60f -> Color(0xFFFFC107) // Yellow - Good
        score >= 40f -> Color(0xFFFF9800) // Orange - Fair
        else -> Color(0xFFF44336)         // Red - Poor
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
 * Compact statistic item for quick summary display.
 *
 * @param icon Icon representing the metric
 * @param value Formatted numeric value
 * @param unit Measurement unit
 * @param label Metric name
 */
@Composable
fun QuickStatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    unit: String,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
