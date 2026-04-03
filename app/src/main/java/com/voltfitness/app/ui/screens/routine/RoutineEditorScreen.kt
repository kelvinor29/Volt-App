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
import androidx.compose.ui.unit.dp
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
 * Entry point for the Routine Editor screen.
 *
 * Connects [RoutineEditorViewModel] to the stateless [RoutineEditorContent],
 * handles TopAppBar configuration, and collects one-time UI effects
 * (navigation, snackbar messages) in a lifecycle-aware manner.
 *
 * ## Exercise selection result
 * When the user returns from the Exercise Picker, the result arrives via
 * [SavedStateHandle] as two keys:
 * - `"exercise_selection_result"` — `List<String>` of ExerciseDB IDs.
 * - `"day_index_for_selection"`  — `Int` index of the target day.
 *
 * Both are observed as [StateFlow]s so the result is never missed on
 * configuration change or process death.
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

    // ── Exercise selection result ─────────────────────────────────────────────
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
        navBackStackEntry?.savedStateHandle?.remove<List<String>>("exercise_selection_result")
        navBackStackEntry?.savedStateHandle?.remove<Int>("day_index_for_selection")
    }

    // ── TopAppBar ─────────────────────────────────────────────────────────────
    LaunchedEffect(uiState.isNewRoutine) {
        onTopAppBarStateChange(
            TopAppBarState(
                title = if (uiState.isNewRoutine) "New Routine" else "Edit Routine",
                showBackButton = true,
                onBackClick = { navController.popBackStack() },
                collapsibleContent = null,
                isCollapsibleVisible = { false }
            )
        )
    }

    // ── One-time effects ──────────────────────────────────────────────────────
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is RoutineEditorEffect.NavigateBack ->
                        navController.popBackStack()

                    is RoutineEditorEffect.ShowError ->
                        snackbarHostState.showSnackbar(effect.message)

                    is RoutineEditorEffect.ShowSuccess ->
                        snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            VoltStepBottomBar(
                primaryText = if (uiState.routineId == -1L) "Save" else "Update",
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
                navController.currentBackStackEntry?.savedStateHandle
                    ?.set("day_index_for_selection", dayIndex)
                navController.navigate(Screen.ExercisePicker.createRoute(routineId, dayOrder))
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}

// =============================================================================
// STATELESS CONTENT
// =============================================================================

/**
 * Stateless content composable — receives all data via [uiState] and
 * communicates user actions back via [onEvent].
 *
 * @param onNavigateToExercisePicker Provides the routineId, the **day index**
 * (for the SavedStateHandle round-trip), and the dayOrder (for the route arg).
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
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(VoltSpacing.medium),
        verticalArrangement = Arrangement.spacedBy(VoltSpacing.small)
    ) {
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
                    .padding(bottom = 16.dp)
            )
        }

        item(key = "section_header_days") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Training Days",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { onEvent(RoutineEditorEvent.OnAddDay) }) {
                    Icon(Icons.Default.Add, contentDescription = "Add training day")
                }
            }
        }

        itemsIndexed(
            items = uiState.days,
            key = { index, day -> "day_${day.order}_$index" }
        ) { index, day ->
            ExpandableDayItem(
                day = day,
                isExpanded = uiState.expandedDayIndex == index,
                onExpandClick = {
                    onEvent(RoutineEditorEvent.OnToggleDayExpanded(index))
                },
                onAddExerciseClick = {
                    onNavigateToExercisePicker(uiState.routineId, index, day.order)
                },
                onExerciseOptionsClick = { /* TODO */ }
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