package com.voltfitness.app.ui.screens.routine.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.designsystem.component.VoltButton
import com.voltfitness.app.core.designsystem.component.VoltCheckButton
import com.voltfitness.app.core.designsystem.component.VoltTextField
import com.voltfitness.app.ui.components.cards.VoltDashedPlaceholder
import com.voltfitness.app.ui.screens.exercise.components.ExerciseGifImage
import com.voltfitness.app.ui.screens.routine.RoutineDayUi
import com.voltfitness.app.ui.screens.routine.RoutineExerciseUi

// =============================================================================
// HEADER SECTION
// =============================================================================

/**
 * Editable text fields for the routine's name, description, and goal.
 * Displayed at the top of the editor inside a [Surface] header.
 *
 * @param name Current routine name.
 * @param description Current routine description.
 * @param goal Current training goal.
 * @param onNameChange Callback when the user modifies the name.
 * @param onDescriptionChange Callback when the user modifies the description.
 * @param onGoalChange Callback when the user modifies the goal.
 * @param modifier Modifier for the container Column.
 */
@Composable
fun RoutineHeaderFields(
    name: String,
    description: String,
    goal: String,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onGoalChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        VoltTextField(
            value = name,
            onValueChange = onNameChange,
            label = "Routine name",
        )

        VoltTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = "Description",
        )

        VoltTextField(
            value = goal,
            onValueChange = onGoalChange,
            label = "Goal",
        )
    }
}

/**
 * Reusable styled [OutlinedTextField] matching the Volt design system.
 * Extracted to avoid repeating shape, icon, and color configurations.
 */
@Composable
private fun RoutineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
) {

    VoltTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        leadingIcon = Icons.Default.Edit,
        singleLine = true
    )
}

// =============================================================================
// ACTION BUTTONS
// =============================================================================

/**
 * Row containing the "Main Routine" toggle and "Save" action buttons.
 *
 * @param isMainRoutine Whether the routine is currently marked as the primary routine.
 * @param isSaving Whether a save operation is in progress (disables the button).
 * @param onToggleMain Callback to toggle the main routine flag.
 * @param onSave Callback to initiate the save operation.
 * @param modifier Modifier for the Row container.
 */
@Composable
fun RoutineActionButtons(
    isMainRoutine: Boolean,
    isSaving: Boolean,
    onToggleMain: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        VoltCheckButton(
            label = "Main Routine",
            checked = isMainRoutine,
            onCheckedChange = { onToggleMain() },
            modifier = Modifier.weight(1f),
            enabled = !isSaving
        )

        VoltButton(
            onClick = onSave,
            enabled = !isSaving,
            text = "Save",
            modifier = Modifier.weight(1f),
        )
    }
}

// =============================================================================
// EXPANDABLE DAY ITEM
// =============================================================================

/**
 * An expandable/collapsible section representing a single training day.
 * Shows the day name with an expand arrow; when expanded, displays
 * the list of exercises (if any) and an "Add exercise" placeholder.
 *
 * @param day The [RoutineDayUi] data for this day.
 * @param isExpanded Whether the day section is currently expanded.
 * @param onExpandClick Callback toggling the expanded state.
 * @param onAddExerciseClick Callback when the user wants to add an exercise.
 * @param onExerciseOptionsClick Callback for the exercise options menu.
 * @param modifier Modifier for the root Column.
 */
@Composable
fun ExpandableDayItem(
    day: RoutineDayUi,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    onAddExerciseClick: () -> Unit,
    onExerciseOptionsClick: (RoutineExerciseUi) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Day header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onExpandClick)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = day.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse ${day.name}"
                else "Expand ${day.name}",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        // Expandable content
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Exercise list
                day.exercises.forEach { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onOptionsClick = { onExerciseOptionsClick(exercise) }
                    )
                }

                VoltDashedPlaceholder(
                    text = "Add new exercise",
                    icon = Icons.AutoMirrored.Outlined.NoteAdd,
                    onClick = onAddExerciseClick,
                    modifier = modifier,
                    containerHeight = 80.dp,
                    iconSize = 28.dp
                )
            }
        }
    }
}

// =============================================================================
// EXERCISE CARD
// =============================================================================

/**
 * Displays a single exercise within a routine day.
 * Shows an image placeholder, exercise name, set/rep/weight summary,
 * and an options menu trigger.
 *
 * @param exercise The [RoutineExerciseUi] data to display.
 * @param onOptionsClick Callback for the three-dot options menu.
 * @param modifier Modifier for the root Row.
 */
@Composable
fun ExerciseCard(
    exercise: RoutineExerciseUi,
    onOptionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExerciseGifImage(
            exerciseId = exercise.id.toString(),
            contentDescription = exercise.name,
            size = 60.dp,
            resolution = 180
        )

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${exercise.sets} sets, ${exercise.repsRange} reps, ${exercise.weightRange}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }

        IconButton(
            onClick = onOptionsClick,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Exercise options",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


