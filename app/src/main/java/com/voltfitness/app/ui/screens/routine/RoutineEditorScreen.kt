package com.voltfitness.app.ui.screens.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.voltfitness.app.ui.components.navigation.VoltStepBottomBar
import com.voltfitness.app.ui.navigation.Screen
import com.voltfitness.app.ui.navigation.TopAppBarState
import com.voltfitness.app.ui.screens.routine.components.ExpandableDayItem
import com.voltfitness.app.ui.screens.routine.components.RoutineHeaderFields
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * Main entry point for the Routine Editor.
 *
 * This screen coordinates a complex state flow including:
 * 1. Metadata management for routines (Name, Goal, etc.).
 * 2. Hierarchical day/exercise organization.
 * 3. Result collection from [ExercisePicker] via [SavedStateHandle].
 *
 * @param navController Navigation controller for screen transitions.
 * @param onTopAppBarStateChange Synchronizes the header with the global scaffold.
 * @param viewModel Business logic orchestrator for the editor.
 */
@Composable
fun RoutineEditorScreen(
    navController: NavController,
    onTopAppBarStateChange: (TopAppBarState) -> Unit,
    viewModel: RoutineEditorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current
    val listState = rememberLazyListState()

    // ─── Result Collection from Exercise Picker ───
    val navBackStackEntry = navController.currentBackStackEntry

    val selectionResult by (
            navBackStackEntry?.savedStateHandle
                ?.getStateFlow<List<String>?>("exercise_selection_result", null)
                ?.collectAsState()
                ?: remember { mutableStateOf(null) }
            )

    val targetDayIndex by (
            navBackStackEntry?.savedStateHandle
                ?.getStateFlow("day_index_for_selection", -1)
                ?.collectAsState()
                ?: remember { mutableStateOf(-1) }
            )

    LaunchedEffect(selectionResult) {
        val ids = selectionResult ?: return@LaunchedEffect
        val dayIndex = targetDayIndex.takeIf { it >= 0 } ?: return@LaunchedEffect

        viewModel.onEvent(
            RoutineEditorEvent.OnExercisesSelectedForDay(
                exerciseIds = ids,
                dayIndex = dayIndex
            )
        )
        // Consume the results once processed to avoid re-triggering on rotation
        navBackStackEntry?.savedStateHandle?.remove<List<String>>("exercise_selection_result")
        navBackStackEntry?.savedStateHandle?.remove<Int>("day_index_for_selection")
    }

    // ─── Global UI State Sync ───
    LaunchedEffect(uiState.isNewRoutine) {
        onTopAppBarStateChange(
            TopAppBarState(
                title = if (uiState.isNewRoutine) "New Routine" else "Edit Routine",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        )
    }

    // ─── Side-Effect Collection ───
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is RoutineEditorEffect.NavigateBack -> navController.popBackStack()
                    is RoutineEditorEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
                    is RoutineEditorEffect.ShowSuccess -> snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            VoltStepBottomBar(
                primaryText = if (uiState.isNewRoutine) "Save" else "Update",
                primaryIcon = Icons.Filled.Save,
                primaryEnabled = uiState.isFormValid,
                primaryLoading = uiState.isSaving,
                onSecondaryClick = { navController.popBackStack() },
                onPrimaryClick = { viewModel.onEvent(RoutineEditorEvent.OnSave) }
            )
        }
    ) { innerPadding ->
        RoutineEditorContent(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            listState = listState,
            onNavigateToExercisePicker = { routineId, dayIndex, dayOrder ->
                navBackStackEntry?.savedStateHandle?.set("day_index_for_selection", dayIndex)
                navController.navigate(Screen.ExercisePicker.createRoute(routineId, dayOrder))
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}

/**
 * Stateless content layer for the Routine Editor.
 *
 * Encapsulates the visual structure using a [LazyColumn] to handle potentially
 * large exercise lists.
 */
@Composable
private fun RoutineEditorContent(
    uiState: RoutineEditorUiState,
    onEvent: (RoutineEditorEvent) -> Unit,
    listState: LazyListState,
    onNavigateToExercisePicker: (routineId: Long, dayIndex: Int, dayOrder: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(VoltSpacing.medium),
        verticalArrangement = Arrangement.spacedBy(VoltSpacing.small)
    ) {
        // --- Routine Global Metadata ---
        item(key = "routine_header_fields") {
            RoutineHeaderFields(
                name = uiState.routineName,
                description = uiState.description,
                goal = uiState.goal,
                onNameChange = { onEvent(RoutineEditorEvent.OnNameChanged(it)) },
                onDescriptionChange = { onEvent(RoutineEditorEvent.OnDescriptionChanged(it)) },
                onGoalChange = { onEvent(RoutineEditorEvent.OnGoalChanged(it)) },
                onToggleMain = { onEvent(RoutineEditorEvent.OnToggleMainRoutine) },
                isMainRoutine = uiState.isMainRoutine,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = VoltSpacing.medium)
            )
        }

        // --- Training Days Section Header ---
        item(key = "section_header_days") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = VoltSpacing.small),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Training Days",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                IconButton(onClick = { onEvent(RoutineEditorEvent.OnAddDay) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add training day",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        // --- Hierarchical Days/Exercises List ---
        itemsIndexed(
            items = uiState.days,
            key = { index, day -> "day_${day.order}_$index" }
        ) { index, day ->
            ExpandableDayItem(
                day = day,
                isExpanded = uiState.expandedDayIndex == index,
                onExpandClick = { onEvent(RoutineEditorEvent.OnToggleDayExpanded(index)) },
                onAddExerciseClick = {
                    onNavigateToExercisePicker(uiState.routineId, index, day.order)
                },
                onExerciseOptionsClick = { /* TODO: Implementation for exercise actions (Delete/Reorder) */ }
            )
        }
    }
}

// =============================================================================
// PREVIEWS
// =============================================================================

@Preview(name = "Edit Mode", showBackground = true, showSystemUi = true)
@Composable
private fun RoutineEditorEditPreview() {
    VoltTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RoutineEditorContent(
                uiState = RoutineEditorUiState(
                    routineId = 1L,
                    isNewRoutine = false,
                    routineName = "Hypertrophy routine",
                    description = "Progressive overload program",
                    goal = "Hypertrophy",
                    days = listOf(
                        RoutineDayUi(
                            id = 1L,
                            order = 1,
                            name = "Day 1",
                            exercises = listOf(
                                RoutineExerciseUi(
                                    exerciseDbId = "0001",
                                    name = "Barbell bench press",
                                    sets = 4,
                                    repsRange = "8-10",
                                    weightRange = "60-80 kg"
                                )
                            )
                        ),
                        RoutineDayUi(id = 2L, order = 2, name = "Day 2")
                    )
                ),
                onEvent = {},
                onNavigateToExercisePicker = { _, _, _ -> },
                listState = rememberLazyListState()
            )
        }
    }
}