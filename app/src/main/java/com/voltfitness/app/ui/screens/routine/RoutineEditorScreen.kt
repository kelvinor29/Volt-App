package com.voltfitness.app.ui.screens.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.derivedStateOf
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
import com.voltfitness.app.domain.model.Exercise
import com.voltfitness.app.ui.components.navigation.VoltStepBottomBar
import com.voltfitness.app.ui.navigation.Screen
import com.voltfitness.app.ui.navigation.TopAppBarState
import com.voltfitness.app.ui.screens.routine.components.ExpandableDayItem
import com.voltfitness.app.ui.screens.routine.components.RoutineHeaderFields
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme


@Composable
fun RoutineEditorScreen(
    navController: NavController,
    onTopAppBarStateChange: (TopAppBarState) -> Unit,
    viewModel: RoutineEditorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val navBackStackEntry = navController.currentBackStackEntry

    val selectionResult by navBackStackEntry?.savedStateHandle
        ?.getStateFlow<List<String>?>("exercise_selection_result", null)
        ?.collectAsState(initial = null) ?: remember { mutableStateOf(null) }

    LaunchedEffect(selectionResult) {
        selectionResult?.let { ids ->
            viewModel.onEvent(RoutineEditorEvent.OnExercisesSelectedForDay(ids))
            navBackStackEntry?.savedStateHandle?.remove<List<String>>("exercise_selection_result")
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    val listState = rememberLazyListState()

    val isHeaderVisible by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) false
            else listState.firstVisibleItemScrollOffset < 150
        }
    }

    LaunchedEffect(
        uiState.isNewRoutine,
        uiState.routineName,
        uiState.description,
        uiState.goal,
        isHeaderVisible
    ) {
        onTopAppBarStateChange(
            TopAppBarState(
                title = if (uiState.isNewRoutine) "New Routine" else "Edit Routine",
                showBackButton = true,
                onBackClick = { navController.popBackStack() },
                collapsibleContent = {
                    RoutineHeaderFields(
                        name = uiState.routineName,
                        description = uiState.description,
                        goal = uiState.goal,
                        onNameChange = { viewModel.onEvent(RoutineEditorEvent.OnNameChanged(it)) },
                        onDescriptionChange = {
                            viewModel.onEvent(RoutineEditorEvent.OnDescriptionChanged(it))
                        },
                        onGoalChange = { viewModel.onEvent(RoutineEditorEvent.OnGoalChanged(it)) },
                        onToggleMain = { viewModel.onEvent(RoutineEditorEvent.OnToggleMainRoutine) },
                        isMainRoutine = uiState.isMainRoutine,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                },
                isCollapsibleVisible = { isHeaderVisible }
            )
        )
    }

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
            onNavigateToExercisePicker = { routineId, dayOrder ->
                navController.navigate(Screen.ExercisePicker.createRoute(routineId, dayOrder))
            },
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}

/**
 * Stateless content composable. Receives all data via [uiState]
 * and communicates user actions via [onEvent].
 */
@Composable
private fun RoutineEditorContent(
    uiState: RoutineEditorUiState,
    onEvent: (RoutineEditorEvent) -> Unit,
    listState: LazyListState,
    onNavigateToExercisePicker: (routineId: Long, dayOrder: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(VoltSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(VoltSpacing.small)
        ) {
            item(key = "section_header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Routines",
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
                        onNavigateToExercisePicker(uiState.routineId, day.order)
                    },
                    onExerciseOptionsClick = {/* TODO: EXERCISE OPTIONS */}
                )
            }
        }
    }
}

@Preview(name = "Edit Mode", showBackground = true, showSystemUi = true)
@Composable
private fun RoutineEditorEditPreview() {
    VoltTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RoutineEditorContent(
                uiState = RoutineEditorUiState(
                    routineName = "Hypertrophy routine",
                    description = "Progressive overload program",
                    goal = "Hypertrophy",
                    days = listOf(
                        RoutineDayUi(
                            order = 1, name = "Day 1",
                            exercises = listOf(
                                Exercise(
                                    id = "2",
                                    name = "Bench press",
                                    bodyPart = "Chest",
                                    target = "Pectorals",
                                    equipment = "Barbell",
                                    secondaryMuscles = listOf("Shoulder"),
                                    instructions = listOf("Lie down on the bench"),
                                    description = null,
                                    difficulty = null,
                                    category = null,
                                    gifUrl = "",
                                )
                            )
                        ),
                        RoutineDayUi(order = 2, name = "Day 2")
                    ),
                    isNewRoutine = false,
                    error = "",
                ),
                onEvent = {},
                onNavigateToExercisePicker = { _, _ -> },
                listState = rememberLazyListState()
            )
        }
    }
}