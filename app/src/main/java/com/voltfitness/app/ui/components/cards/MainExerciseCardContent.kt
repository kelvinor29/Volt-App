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
import com.voltfitness.app.ui.components.VoltCard
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * Data representation for an exercise summary.
 *
 * @property id Unique identifier.
 * @property name Title of the exercise or muscle group.
 * @property description Secondary information (e.g., Level or Equipment).
 * @property intensity Visual metric like calories or effort level.
 */
data class ExerciseSummary(
    val id: Long,
    val name: String,
    val description: String,
    val intensity: String
)

/**
 * Main exercise card component for dashboard and workout lists.
 *
 * @param exercise The [ExerciseSummary] data model.
 * @param onStartClick Action triggered by the "Start" button.
 * @param modifier Layout modifier.
 */
@Composable
fun MainExerciseCard(
    exercise: ExerciseSummary,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    VoltCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(VoltSpacing.medium)
                .fillMaxWidth()
        ) {
            ExerciseHeader(exercise)

            Spacer(modifier = Modifier.height(VoltSpacing.small))

            Button(
                onClick = onStartClick,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Start Workout",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

/**
 * Internal header for the exercise card containing title, description, and intensity.
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

        // Intensity Indicator
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.ic_fire_flame),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = exercise.intensity,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(name = "Main Exercise Card States")
@Composable
private fun MainExerciseCardPreview() {
    val exercise =
        ExerciseSummary(1, "Chest + Triceps", "Intermediate - Basic Routine", "450 kcal")

    VoltTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.padding(16.dp)) {
                MainExerciseCard(exercise = exercise, onStartClick = {})
            }
        }
    }
}