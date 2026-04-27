package com.voltfitness.app.ui.screens.routine.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.common.ProfileOptions
import com.voltfitness.app.core.designsystem.component.VoltCheckButton
import com.voltfitness.app.core.designsystem.component.VoltDropdownSelector
import com.voltfitness.app.core.designsystem.component.VoltTextField
import com.voltfitness.app.ui.components.cards.ExerciseCard
import com.voltfitness.app.ui.components.cards.ExerciseCardTrailing
import com.voltfitness.app.ui.components.cards.VoltDashedPlaceholder
import com.voltfitness.app.ui.screens.routine.RoutineDayUi
import com.voltfitness.app.ui.screens.routine.RoutineExerciseUi

/**
 * Editable fields for the routine's name, description, goal, and main flag.
 *
 * @param name Current routine name.
 * @param description Current routine description.
 * @param goal Current training goal.
 * @param isMainRoutine Whether this is the user's active main routine.
 * @param onNameChange Callback when the user edits the name.
 * @param onDescriptionChange Callback when the user edits the description.
 * @param onGoalChange Callback when the user changes the goal.
 * @param onToggleMain Callback when the user toggles the "Set Main Routine" check.
 * @param modifier Modifier for the container [Column].
 */
@Composable
fun RoutineHeaderFields(
    name: String,
    description: String,
    goal: String,
    isMainRoutine: Boolean,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onGoalChange: (String) -> Unit,
    onToggleMain: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        VoltTextField(value = name, onValueChange = onNameChange, label = "Routine name")
        VoltTextField(value = description, onValueChange = onDescriptionChange, label = "Description")
        VoltDropdownSelector(
            label = "Training Goal",
            options = ProfileOptions.goals,
            selectedOption = goal,
            onOptionSelected = onGoalChange,
        )
        VoltCheckButton(
            label = "Set Main Routine",
            checked = isMainRoutine,
            onCheckedChange = onToggleMain,
        )
    }
}

/**
 * An expandable/collapsible section representing a single training day.
 *
 * When expanded, it renders the day's exercise list using [ExerciseCard]
 * (with [ExerciseCardTrailing.OptionsMenu]) and an "Add exercise" dashed
 * placeholder at the bottom.
 *
 * Each [ExerciseCard] receives:
 * - [RoutineExerciseUi.exerciseDbId] as the `exerciseId` for the GIF image,
 *   keeping the same data source as the Exercise Picker.
 * - [buildEditorSubtitle] for the subtitle, centralising the formatting logic.
 *
 * @param day The [RoutineDayUi] data for this section.
 * @param isExpanded Whether the content is visible.
 * @param onExpandClick Toggles the expanded state.
 * @param onAddExerciseClick Opens the Exercise Picker for this day.
 * @param onExerciseOptionsClick Invoked with the exercise when MoreVert is tapped.
 * @param modifier Modifier for the root [Column].
 */
@Composable
fun ExpandableDayItem(
    day: RoutineDayUi,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    onAddExerciseClick: () -> Unit,
    onExerciseOptionsClick: (RoutineExerciseUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onExpandClick)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp),
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = day.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant,
        )

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically(),
        ) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                day.exercises.forEach { exercise ->
                    ExerciseCard(
                        exerciseId = exercise.exerciseDbId,
                        exerciseName = exercise.name,
                        subtitle = buildEditorSubtitle(exercise),
                        trailing = ExerciseCardTrailing.OptionsMenu(
                            onClick = { onExerciseOptionsClick(exercise) }
                        )
                    )
                }

                VoltDashedPlaceholder(
                    text = "Add new exercise",
                    icon = Icons.AutoMirrored.Outlined.NoteAdd,
                    onClick = onAddExerciseClick,
                    modifier = modifier,
                    containerHeight = 80.dp,
                    iconSize = 28.dp,
                )
            }
        }
    }
}

private fun buildEditorSubtitle(exercise: RoutineExerciseUi): String {
    return "${exercise.sets} sets · ${exercise.repsRange} reps · ${exercise.weightRange}"
}