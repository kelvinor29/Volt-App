package com.voltfitness.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isEmpty
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.voltfitness.app.ui.screens.body_composition.BodyCompositionScreen
import com.voltfitness.app.ui.screens.body_composition.BodyCompositionViewModel
import com.voltfitness.app.ui.screens.body_composition.add.AddBodyCompositionScreen
import com.voltfitness.app.ui.screens.exercise.ExercisePickerScreen
import com.voltfitness.app.ui.screens.exercise.ExercisePickerViewModel
import com.voltfitness.app.ui.screens.home.HomeScreen
import com.voltfitness.app.ui.screens.register.RegisterNavigationEffect
import com.voltfitness.app.ui.screens.register.RegisterNavigationEffect.*
import com.voltfitness.app.ui.screens.register.RegisterScreen
import com.voltfitness.app.ui.screens.register.RegisterViewModel
import com.voltfitness.app.ui.screens.routine.RoutineEditorScreen
import com.voltfitness.app.ui.screens.workout.WorkoutDetailScreen
import com.voltfitness.app.ui.session.SessionState
import com.voltfitness.app.ui.session.SessionViewModel

/**
 * Navigation Graph for the VoltFitness application.
 *
 * The startDestination is pre-resolved by the calling composable (VoltApp)
 * based on SessionState. This graph contains NO auth logic — it is a pure
 * navigation structure.
 *
 * @param navController The controller handling navigation stack.
 * @param topAppBarState A mutable state used to sync the TopAppBar UI.
 * @param sessionViewModel Activity-scoped ViewModel for session state updates.
 * @param modifier Layout modifier for the NavHost container.
 * @param startDestination Pre-resolved route: Home or Register.
 */
@Composable
fun VoltNavGraph(
    navController: NavHostController,
    topAppBarState: MutableState<TopAppBarState>,
    sessionViewModel: SessionViewModel,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // ========== SPLASH / AUTH GUARD ==========
        composable(route = Screen.Splash.route) {
            val sessionState by sessionViewModel.sessionState.collectAsState()

            LaunchedEffect(sessionState) {
                when (sessionState) {
                    is SessionState.Authenticated -> {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }

                    is SessionState.Unauthenticated -> {
                        navController.navigate(Screen.Register.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }

                    else -> Unit
                }
            }

            if (sessionState is SessionState.Loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }

            }
        }

        // ========== REGISTER SCREEN ==========
        composable(route = Screen.Register.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                topAppBarState.value = TopAppBarState(
                    title = "",
                    showBackButton = false
                )
            }

            LaunchedEffect(Unit) {
                viewModel.navigationEffect.collect { effect ->
                    when (effect) {
                        is RegistrationComplete -> {
                            sessionViewModel.onUserRegistered()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                            navController.navigate(Screen.BodyComposition.route)
                        }

                    }
                }
            }

            RegisterScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent
            )
        }

        // ========== HOME SCREEN ==========
        composable(route = Screen.Home.route) {
            HomeScreen(
                navController = navController,
                onTopAppBarStateChange = { newState ->
                    topAppBarState.value = newState
                }
            )
        }

        // ========== WORKOUT DETAIL SCREEN ==========
        composable(
            route = Screen.WorkoutDetail.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.StringType })
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId") ?: ""

            WorkoutDetailScreen(
                workoutId = workoutId,
                navController = navController,
                onTopAppBarStateChange = { newState ->
                    topAppBarState.value = newState
                }
            )
        }

        // ========== ROUTINE DETAIL SCREEN (Edit existing) ==========
        composable(
            route = Screen.RoutineDetail.route,
            arguments = listOf(navArgument("routineId") { type = NavType.StringType })
        ) {
            RoutineEditorScreen(
                navController = navController,
                onTopAppBarStateChange = { topAppBarState.value = it }
            )
        }

        // ========== ROUTINE CREATOR SCREEN (Create new in folder) ==========
        composable(
            route = Screen.RoutineCreator.route,
            arguments = listOf(navArgument("folderId") { type = NavType.LongType })
        ) { backStackEntry ->

            LaunchedEffect(Unit) {
                topAppBarState.value = TopAppBarState(
                    title = "New Routine",
                    subtitle = "Create your workout plan",
                    showBackButton = true,
                    onBackClick = { navController.popBackStack() }
                )
            }

            RoutineEditorScreen(
                navController = navController,
                onTopAppBarStateChange = { topAppBarState.value = it },
                viewModel = hiltViewModel()
            )
        }

        // ========== BODY COMPOSITION SCREEN ==========
        composable(route = Screen.BodyComposition.route) { backStackEntry ->
            val viewModel: BodyCompositionViewModel = hiltViewModel(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                topAppBarState.value = TopAppBarState(
                    title = "Body Composition",
                    subtitle = "Track your physical progress",
                    showBackButton = true,
                    onBackClick = { navController.popBackStack() }
                )
            }

            BodyCompositionScreen(
                uiState = uiState,
                onAddNewMeasurement = {
                    viewModel.resetAddForm()
                    navController.navigate(Screen.AddBodyComposition.route)
                },
                onEntryClick = { },
                navController = navController,
            )
        }

        // ========== ADD BODY COMPOSITION SCREEN ==========
        composable(route = Screen.AddBodyComposition.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                try {
                    navController.getBackStackEntry(Screen.BodyComposition.route)
                } catch (e: Exception) {
                    backStackEntry
                }
            }
            val viewModel: BodyCompositionViewModel = hiltViewModel(parentEntry)
            val addUiState by viewModel.addUiState.collectAsState()

            LaunchedEffect(Unit) {
                topAppBarState.value = TopAppBarState(
                    title = "New Measurement",
                    subtitle = "Enter your current metrics",
                    showBackButton = true,
                    onBackClick = { navController.popBackStack() }
                )
            }

            val historyState by viewModel.uiState.collectAsState()

            AddBodyCompositionScreen(
                uiState = addUiState,
                onEvent = viewModel::onAddEvent,
                onNavigateBack = {
                    if (historyState.historyEntries.isEmpty()) {
                        navController.popBackStack(Screen.Home.route, inclusive = false)
                    } else {
                        navController.popBackStack()
                    }
                })
        }

        // ========== EXERCISE PICKER SCREEN ==========
        composable(
            route = Screen.ExercisePicker.route,
            arguments = listOf(
                navArgument("routineId") { type = NavType.LongType },
                navArgument("dayOrder") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val viewModel: ExercisePickerViewModel = hiltViewModel(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                topAppBarState.value = TopAppBarState(
                    title = "Add exercise",
                    showBackButton = true,
                    onBackClick = { navController.popBackStack() }
                )
            }

            ExercisePickerScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onConfirmSelection = { selectedIds ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("exercise_selection_result", selectedIds)

                    navController.popBackStack()
                }
            )
        }


        // ========== FUTURE SCREENS (Placeholders) ==========

        composable(route = Screen.Settings.route) {
            // TODO: Implement SettingsScreen
        }
    }
}