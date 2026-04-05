package com.voltfitness.app.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.designsystem.component.voltFieldModifier
import com.voltfitness.app.core.designsystem.component.voltFieldShape
import com.voltfitness.app.ui.screens.exercise.components.ExerciseGifImage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.tooling.preview.Preview
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * Defines the contract for trailing actions within an [ExerciseCard].
 * Using a sealed interface prevents illegal UI states and simplifies caller intent.
 */
sealed interface ExerciseCardTrailing {
    /** Toggle state for selection lists. */
    data class SelectionToggle(val isSelected: Boolean) : ExerciseCardTrailing
    /** Interactive menu for management actions. */
    data class OptionsMenu(val onClick: () -> Unit) : ExerciseCardTrailing
}

/**
 * Highly reusable exercise row component used across Picker and Editor contexts.
 *
 * Features a stateless design that accepts pre-formatted strings to avoid
 * coupling with specific domain models.
 *
 * @param exerciseId Unique ID for remote GIF loading.
 * @param exerciseName Primary label.
 * @param subtitle Contextual metadata (e.g., body part or set summary).
 * @param trailing Specific [ExerciseCardTrailing] implementation.
 * @param onClick Optional card-level interaction.
 */
@Composable
fun ExerciseCard(
    exerciseId: String,
    exerciseName: String,
    subtitle: String,
    trailing: ExerciseCardTrailing,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    tonalElevation: Dp = 1.dp,
    gifSize: Dp = 60.dp,
) {
    Surface(
        modifier = modifier
            .voltFieldModifier()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = voltFieldShape,
        tonalElevation = tonalElevation,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ExerciseGifImage(
                exerciseId = exerciseId,
                contentDescription = exerciseName,
                size = gifSize,
                resolution = 180,
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exerciseName.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(Modifier.width(8.dp))

            ExerciseCardTrailingSlot(trailing = trailing)
        }
    }
}

/**
 * Internal slot switcher for trailing actions.
 */
@Composable
private fun ExerciseCardTrailingSlot(trailing: ExerciseCardTrailing) {
    when (trailing) {
        is ExerciseCardTrailing.SelectionToggle -> {
            Icon(
                imageVector = if (trailing.isSelected) Icons.Default.CheckCircle
                else Icons.Default.Info,
                contentDescription = null,
                tint = if (trailing.isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        is ExerciseCardTrailing.OptionsMenu -> {
            IconButton(
                onClick = trailing.onClick,
                modifier = Modifier.size(24.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Exercise Card States")
@Composable
fun PreviewExerciseCardStates() {
    VoltTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // State 1: Selection Toggle (Unselected)
            ExerciseCard(
                exerciseId = "1",
                exerciseName = "bench press",
                subtitle = "Chest • Barbell",
                trailing = ExerciseCardTrailing.SelectionToggle(isSelected = false),
                onClick = { }
            )

            // State 2: Selection Toggle (Selected)
            ExerciseCard(
                exerciseId = "2",
                exerciseName = "deadlift",
                subtitle = "Back • Barbell",
                trailing = ExerciseCardTrailing.SelectionToggle(isSelected = true),
                onClick = { }
            )

            // State 3: Options Menu
            ExerciseCard(
                exerciseId = "3",
                exerciseName = "pull ups",
                subtitle = "Bodyweight • 3 sets x 12 reps",
                trailing = ExerciseCardTrailing.OptionsMenu(onClick = { }),
                onClick = { }
            )
        }
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewExerciseCardDark() {
    VoltTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ExerciseCard(
                exerciseId = "4",
                exerciseName = "squats",
                subtitle = "Legs • Quadriceps",
                trailing = ExerciseCardTrailing.OptionsMenu(onClick = { }),
                onClick = { }
            )
        }
    }
}