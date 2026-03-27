package com.voltfitness.app.ui.screens.exercise

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.designsystem.component.VoltTextField
import com.voltfitness.app.domain.model.Exercise
import com.voltfitness.app.ui.screens.exercise.components.ExerciseGifImage
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * Stateless UI for the Exercise Picker screen.
 *
 * @param uiState Current [ExercisePickerUiState] from the ViewModel.
 * @param onEvent Callback for user actions.
 * @param onConfirmSelection Called when user taps the "Add" button in the top bar.
 */
@Composable
fun ExercisePickerScreen(
    uiState: ExercisePickerUiState,
    onEvent: (ExercisePickerEvent) -> Unit,
    onConfirmSelection: (List<String>) -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
                .fillMaxSize()
        ) {
            // Search field
            VoltTextField(
                value = uiState.query,
                onValueChange = { onEvent(ExercisePickerEvent.OnQueryChanged(it)) },
                label = "Search exercise",
                leadingIcon = Icons.Outlined.Search,
                singleLine = true
            )

            // Tab chips (All / Muscles / Equipment)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = uiState.filterTab == ExerciseFilterTab.All,
                    onClick = { onEvent(ExercisePickerEvent.OnFilterTabChanged(ExerciseFilterTab.All)) },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = uiState.filterTab == ExerciseFilterTab.Muscles,
                    onClick = { onEvent(ExercisePickerEvent.OnFilterTabChanged(ExerciseFilterTab.Muscles)) },
                    label = { Text("Muscles") }
                )
                FilterChip(
                    selected = uiState.filterTab == ExerciseFilterTab.Equipment,
                    onClick = { onEvent(ExercisePickerEvent.OnFilterTabChanged(ExerciseFilterTab.Equipment)) },
                    label = { Text("Equipment") }
                )
            }

            // Sub-filter chips — shown when Muscles or Equipment tab is active
            when (uiState.filterTab) {
                ExerciseFilterTab.Muscles -> {
                    CatalogChipRow(
                        items = uiState.bodyParts,
                        selectedItem = uiState.selectedBodyPart,
                        onItemClick = { onEvent(ExercisePickerEvent.OnBodyPartSelected(it)) }
                    )
                }

                ExerciseFilterTab.Equipment -> {
                    CatalogChipRow(
                        items = uiState.equipment,
                        selectedItem = uiState.selectedEquipment,
                        onItemClick = { onEvent(ExercisePickerEvent.OnEquipmentSelected(it)) }
                    )
                }

                ExerciseFilterTab.All -> { /* No sub-filters */
                }
            }

            Spacer(Modifier.height(8.dp))

            // Exercise list or loading
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.exercises, key = { it.id }) { exercise ->
                        ExercisePickerItem(
                            exercise = exercise,
                            isSelected = uiState.selectedExerciseIds.contains(exercise.id),
                            onClick = {
                                onEvent(ExercisePickerEvent.OnToggleSelection(exercise.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Horizontally scrollable row of catalog filter chips.
 * Tapping a selected chip deselects it (passes null).
 *
 * @param items The catalog values (e.g., ["chest", "back", "legs"]).
 * @param selectedItem The currently active filter, or null if none.
 * @param onItemClick Callback — passes the value on select, null on deselect.
 */
@Composable
private fun CatalogChipRow(
    items: List<String>,
    selectedItem: String?,
    onItemClick: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.padding(top = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            FilterChip(
                selected = item == selectedItem,
                onClick = {
                    onItemClick(if (item == selectedItem) null else item)
                },
                label = {
                    Text(
                        text = item.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}

/**
 * Single row item for the exercise picker list.
 */
@Composable
private fun ExercisePickerItem(
    exercise: Exercise,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = if (isSelected) 4.dp else 1.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExerciseGifImage(
                exerciseId = exercise.id,
                contentDescription = exercise.name,
                size = 60.dp,
                resolution = 180
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${exercise.bodyPart} · ${exercise.target} · ${exercise.equipment}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = if (isSelected) Icons.Default.CheckCircle
                else Icons.Default.Info,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


// region Previews

@Preview(name = "Picker - Data Loaded", showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun ExercisePickerScreenPreview() {
    val mockExercises = listOf(
        Exercise(
            "1", "Barbell Bench Press", "chest", "pectorals", "barbell",
            listOf("triceps"), listOf("Lie down", "Push"), null, null, null, null
        ),
        Exercise(
            "2", "Dumbbell Lateral Raise", "shoulders", "deltoids", "dumbbell",
            emptyList(), listOf("Lift sideways"), null, null, null, null
        ),
        Exercise(
            "3", "Cable Pulldown", "back", "lats", "cable",
            listOf("biceps"), listOf("Pull down"), null, null, null, null
        )
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
                equipment = listOf("barbell", "dumbbell", "cable", "body weight")
            ),
            onEvent = {},
            onConfirmSelection = {}
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
            onConfirmSelection = {}
        )
    }
}

// endregion
