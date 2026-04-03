package com.voltfitness.app.ui.components.cards

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.voltfitness.app.ui.screens.exercise.components.ExerciseGifImage

/**
 * Defines the trailing action area of an [ExerciseCard].
 *
 * Keeping the trailing slot as a sealed type instead of a generic composable
 * lambda lets callers express intent clearly and prevents misuse.
 */
sealed interface ExerciseCardTrailing {

    /**
     * A check/info icon toggle used in the Exercise Picker.
     *
     * @param isSelected Whether the exercise is currently selected.
     */
    data class SelectionToggle(val isSelected: Boolean) : ExerciseCardTrailing

    /**
     * A three-dot menu icon used in the Routine Editor.
     *
     * @param onClick Invoked when the user taps the options button.
     */
    data class OptionsMenu(val onClick: () -> Unit) : ExerciseCardTrailing
}

/**
 * Shared exercise row card used in both [ExercisePickerScreen] and
 * [RoutineEditorComponents].
 *
 * The card layout is always:
 *   [GIF thumbnail] — [Name + subtitle text] — [Trailing action]
 *
 * The only visual differences between the two contexts are:
 * - The **subtitle** text (bodyPart/target in Picker; sets/reps/weight in Editor)
 * - The **trailing** widget ([ExerciseCardTrailing])
 * - The card **surface elevation** (selected state in Picker)
 *
 * Both contexts pass pre-formatted strings to keep this composable
 * fully stateless and reusable without coupling it to either domain model.
 *
 * @param exerciseId    ExerciseDB ID used to load the animated GIF.
 * @param exerciseName  Display name shown in bold on the first line.
 * @param subtitle      Second line of text (already formatted by the caller).
 * @param trailing      The action widget rendered at the end of the row.
 * @param onClick       Optional card-level click (used in Picker to toggle selection).
 * @param tonalElevation Surface tonal elevation (bump for selected state in Picker).
 * @param gifSize       Size of the [ExerciseGifImage] thumbnail.
 * @param modifier      Modifier for the root [Surface].
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
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = tonalElevation,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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

@Composable
private fun ExerciseCardTrailingSlot(trailing: ExerciseCardTrailing) {
    when (trailing) {
        is ExerciseCardTrailing.SelectionToggle -> {
            Icon(
                imageVector = if (trailing.isSelected) Icons.Default.CheckCircle
                else Icons.Default.Info,
                contentDescription = if (trailing.isSelected) "Selected" else "Details",
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
                    contentDescription = "Exercise options",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}