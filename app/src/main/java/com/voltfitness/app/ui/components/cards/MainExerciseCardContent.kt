package com.voltfitness.app.ui.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.R
import com.voltfitness.app.core.designsystem.component.VoltButton // Reusing existing component
import com.voltfitness.app.core.designsystem.component.voltSectionShape
import com.voltfitness.app.ui.components.VoltCard
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * Domain-specific summary for exercise or routine highlighting.
 *
 * @property id Identifier for navigation or selection.
 * @property name Primary title (e.g., "Full Body Blast").
 * @property description Metadata summary (e.g., "Intermediate • 45 min").
 * @property intensity Visual metric value (e.g., "500 kcal").
 */
data class ExerciseSummary(
    val id: Long,
    val name: String,
    val description: String,
    val intensity: String
)

/**
 * Featured exercise card used in dashboards to promote or resume workouts.
 *
 * @param exercise The [ExerciseSummary] state.
 * @param onStartClick Execution trigger for the workout session.
 */
@Composable
fun MainExerciseCard(
    exercise: ExerciseSummary,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    VoltCard(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(VoltSpacing.medium)
                .fillMaxWidth()
        ) {
            ExerciseHeader(exercise)

            Spacer(modifier = Modifier.height(VoltSpacing.medium))

            VoltButton(
                text = "Start Workout",
                onClick = onStartClick,
                icon = Icons.Default.PlayArrow,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Internal layout for the card header, separating title metadata from intensity metrics.
 */
@Composable
private fun ExerciseHeader(exercise: ExerciseSummary) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = exercise.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        IntensityIndicator(value = exercise.intensity)
    }
}

/**
 * Small visual badge for calorie or effort level metrics.
 */
@Composable
private fun IntensityIndicator(value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = R.drawable.ic_fire_flame),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MainExerciseCardPreview() {
    val exercise = ExerciseSummary(
        id = 1,
        name = "Chest + Triceps",
        description = "Intermediate - Basic Routine",
        intensity = "450 kcal"
    )

    VoltTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            MainExerciseCard(exercise = exercise, onStartClick = {})
        }
    }
}