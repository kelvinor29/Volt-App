package com.voltfitness.app.ui.components.cards

import android.view.Surface
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.R
import com.voltfitness.app.ui.screens.home.ActiveRoutineDayUi
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * Displays a compact summary of a single training day within the active routine.
 *
 * Shows the day number as a prominent label, the focused muscle groups as a subtitle,
 * and a primary CTA button to start the workout session.
 *
 * @param day             UI model containing day metadata.
 * @param onStartWorkout  Triggered when the user taps "Start Workout".
 * @param modifier        Outer layout constraints.
 */
@Composable
fun RoutineDayCard(
    day: ActiveRoutineDayUi,
    onStartWorkout: (dayId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(VoltSpacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(VoltSpacing.small)
        ) {
            // Day label — e.g. "Day 1"
            Text(
                text = stringResource(R.string.day, day.dayOrder),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Day name — e.g. "Leg Day"
            Text(
                text = day.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Focused muscle groups
            if (day.focusBodyParts.isNotBlank()) {
                Text(
                    text = day.focusBodyParts,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(VoltSpacing.small))

            // Primary CTA
            Button(
                onClick = { onStartWorkout(day.dayId) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.start_workout))
            }
        }
    }
}

// region Previews
@Preview(showBackground = true)
@Composable
private fun RoutineDayCardPreview() {
    VoltTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RoutineDayCard(
                day = ActiveRoutineDayUi(
                    dayId = 1L,
                    dayOrder = 1,
                    name = "Push Day",
                    focusBodyParts = "Chest, Shoulders, Triceps"
                ),
                onStartWorkout = {},
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }
}
// endregion
