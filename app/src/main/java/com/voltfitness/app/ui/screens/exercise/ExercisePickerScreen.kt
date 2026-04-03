package com.voltfitness.app.ui.screens.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.designsystem.component.VoltTextField
import com.voltfitness.app.domain.model.Exercise
import com.voltfitness.app.ui.components.cards.ExerciseCard
import com.voltfitness.app.ui.components.cards.ExerciseCardTrailing
import com.voltfitness.app.ui.components.navigation.VoltStepBottomBar
import com.voltfitness.app.ui.theme.VoltTheme
import timber.log.Timber

/**
 * Stateless UI for the Exercise Picker screen.
 *
 * Allows the user to browse, search, and multi-select exercises to add
 * to a routine day. Each row delegates to the shared [ExerciseCard] with
 * [ExerciseCardTrailing.SelectionToggle] as its trailing widget.
 *
 * @param uiState           Current [ExercisePickerUiState] from the ViewModel.
 * @param onEvent           Callback for user interactions.
 * @param onConfirmSelection Called when the user confirms the selection.
 * @param onNavigateBack    Callback to dismiss this screen.
 */
@Composable
fun ExercisePickerScreen(
    uiState: ExercisePickerUiState,
    onEvent: (ExercisePickerEvent) -> Unit,
    onConfirmSelection: (List<String>) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val selectedCount = uiState.selectedExerciseIds.size
    val isAnySelected = selectedCount > 0

    Scaffold(
        bottomBar = {
            VoltStepBottomBar(
                primaryText = when {
                    selectedCount > 1 -> "Add $selectedCount exercises"
                    selectedCount == 1 -> "Add 1 exercise"
                    else -> "Select exercises"
                },
                primaryIcon = Icons.Filled.Add,
                primaryEnabled = isAnySelected && !uiState.isLoading,
                primaryLoading = uiState.isLoading,
                secondaryText = "Cancel",
                onSecondaryClick = onNavigateBack,
                onPrimaryClick = { onConfirmSelection(uiState.selectedExerciseIds.toList()) },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
        ) {
            // Search field
            VoltTextField(
                value = uiState.query,
                onValueChange = { onEvent(ExercisePickerEvent.OnQueryChanged(it)) },
                label = "Search exercise",
                leadingIcon = Icons.Outlined.Search,
                singleLine = true,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = uiState.filterTab == ExerciseFilterTab.All,
                    onClick = { onEvent(ExercisePickerEvent.OnFilterTabChanged(ExerciseFilterTab.All)) },
                    label = { Text("All") },
                )
                FilterChip(
                    selected = uiState.filterTab == ExerciseFilterTab.Muscles,
                    onClick = { onEvent(ExercisePickerEvent.OnFilterTabChanged(ExerciseFilterTab.Muscles)) },
                    label = { Text("Muscles") },
                )
                FilterChip(
                    selected = uiState.filterTab == ExerciseFilterTab.Equipment,
                    onClick = { onEvent(ExercisePickerEvent.OnFilterTabChanged(ExerciseFilterTab.Equipment)) },
                    label = { Text("Equipment") },
                )
            }

            when (uiState.filterTab) {
                ExerciseFilterTab.Muscles -> CatalogChipRow(
                    items = uiState.bodyParts,
                    selectedItem = uiState.selectedBodyPart,
                    onItemClick = { onEvent(ExercisePickerEvent.OnBodyPartSelected(it)) },
                )

                ExerciseFilterTab.Equipment -> CatalogChipRow(
                    items = uiState.equipment,
                    selectedItem = uiState.selectedEquipment,
                    onItemClick = { onEvent(ExercisePickerEvent.OnEquipmentSelected(it)) },
                )

                ExerciseFilterTab.All -> Unit
            }

            Spacer(Modifier.height(8.dp))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) { CircularProgressIndicator() }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(uiState.exercises, key = { it.id }) { exercise ->
                        val isSelected = uiState.selectedExerciseIds.contains(exercise.id)

                        ExerciseCard(
                            exerciseId = exercise.id,
                            exerciseName = exercise.name,
                            subtitle = buildPickerSubtitle(exercise),
                            trailing = ExerciseCardTrailing.SelectionToggle(isSelected = isSelected),
                            onClick = { onEvent(ExercisePickerEvent.OnToggleSelection(exercise.id)) },
                            tonalElevation = if (isSelected) 4.dp else 1.dp,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Builds the subtitle string for the Picker context.
 * Each token is individually capitalized so "chest · pectorals · barbell"
 * becomes "Chest · Pectorals · Barbell" regardless of how the API returns them.
 */
private fun buildPickerSubtitle(exercise: Exercise): String {
    val bodyPart = exercise.bodyPart.replaceFirstChar { it.uppercase() }
    val target = exercise.target.replaceFirstChar { it.uppercase() }
    val equipment = exercise.equipment.replaceFirstChar { it.uppercase() }
    return "$bodyPart · $target · $equipment"
}


/**
 * Horizontally scrollable row of catalog filter chips.
 * Tapping an already-selected chip passes `null` to deselect it.
 *
 * @param items        Catalog values (e.g. ["chest", "back", "legs"]).
 * @param selectedItem Currently active filter, or `null` if none.
 * @param onItemClick  Passes the selected value, or `null` on deselect.
 */
@Composable
private fun CatalogChipRow(
    items: List<String>,
    selectedItem: String?,
    onItemClick: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.padding(top = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items) { item ->
            FilterChip(
                selected = item == selectedItem,
                onClick = { onItemClick(if (item == selectedItem) null else item) },
                label = {
                    Text(
                        text = item.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
            )
        }
    }
}

// region Previews

@Preview(name = "Picker - Data Loaded", showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ExercisePickerScreenPreview() {
    val mockExercises = listOf(
        Exercise("1", "barbell bench press", "chest", "pectorals", "barbell", listOf("triceps"), listOf("Lie down", "Push"), null, null, null, null),
        Exercise("2", "dumbbell lateral raise", "shoulders", "deltoids", "dumbbell", emptyList(), listOf("Lift sideways"), null, null, null, null),
        Exercise("3", "cable pulldown", "back", "lats", "cable", listOf("biceps"), listOf("Pull down"), null, null, null, null),
    )
    VoltTheme {
        ExercisePickerScreen(
            uiState = ExercisePickerUiState(
                exercises = mockExercises,
                allExercises = mockExercises,
                selectedExerciseIds = setOf("1"),
                isLoading = false,
                query = "",
                bodyParts = listOf("chest", "back", "shoulders", "legs"),
                equipment = listOf("barbell", "dumbbell", "cable", "body weight"),
            ),
            onEvent = {},
            onConfirmSelection = {},
            onNavigateBack = {},
        )
    }
}

@Preview(name = "Picker - Loading", showBackground = true)
@Composable
private fun ExercisePickerLoadingPreview() {
    VoltTheme {
        ExercisePickerScreen(
            uiState = ExercisePickerUiState(isLoading = true),
            onEvent = {},
            onConfirmSelection = {},
            onNavigateBack = {},
        )
    }
}

// endregion