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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.voltfitness.app.ui.common.UiText
import com.voltfitness.app.R
import com.voltfitness.app.ui.screens.bodycomposition.BodyCompositionNavEffect
import com.voltfitness.app.ui.screens.bodycomposition.BodyCompositionScreen
import com.voltfitness.app.ui.screens.bodycomposition.BodyCompositionViewModel
import com.voltfitness.app.ui.screens.bodycomposition.add.AddBodyCompositionNavEffect
import com.voltfitness.app.ui.screens.bodycomposition.add.AddBodyCompositionScreen
import com.voltfitness.app.ui.screens.bodycomposition.add.AddBodyCompositionViewModel
import com.voltfitness.app.ui.screens.exercise.ExercisePickerScreen
import com.voltfitness.app.ui.screens.exercise.ExercisePickerViewModel
import com.voltfitness.app.ui.screens.home.HomeScreen
import com.voltfitness.app.ui.screens.register.RegisterNavigationEffect.*
import com.voltfitness.app.ui.screens.register.RegisterScreen
import com.voltfitness.app.ui.screens.register.RegisterViewModel
import com.voltfitness.app.ui.screens.routine.RoutineEditorScreen
import com.voltfitness.app.ui.screens.settings.SettingsScreen
import com.voltfitness.app.ui.screens.settings.SettingsViewModel
import com.voltfitness.app.ui.screens.workout.WorkoutDetailScreen
import com.voltfitness.app.ui.session.SessionState
import com.voltfitness.app.ui.session.SessionViewModel

/**
 * Central navigation hub for the VoltFitness application.
 *
 * This graph defines the entry points, argument parsing, and state synchronization
 * between the screens and the global [TopAppBarState].
 *
 * Side-effects from ViewModels (NavigationEffects) are collected here to maintain
 * a one-way data flow (UDF) and keep Composables stateless.
 */
@Composable
fun VoltNavGraph(
    navController: NavHostController,
    topAppBarState: MutableState<TopAppBarState>,
    sessionViewModel: SessionViewModel,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.Splash.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {

        // ========== SPLASH / AUTH GUARD ==========
        /**
         * Orchestrates the initial routing based on the [SessionState].
         * Redirects to Home if authenticated, or Register if not.
         */
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
                    contentAlignment = Alignment.Center,
                ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
            }
        }

        // ========== REGISTER SCREEN ==========
        /**
         * Onboarding flow for new users.
         * On completion, signals the [SessionViewModel] and moves to Home.
         */
        composable(route = Screen.Register.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                topAppBarState.value = TopAppBarState(title = UiText.Empty, showBackButton = false)
            }

            LaunchedEffect(Unit) {
                viewModel.navigationEffect.collect { effect ->
                    when (effect) {
                        is RegistrationComplete -> {
                            sessionViewModel.onUserRegistered()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Register.route) { inclusive = true }
                            }
                            // Direct flow to setup initial body composition
                            navController.navigate(Screen.BodyComposition.route)
                        }
                    }
                }
            }

            RegisterScreen(uiState = uiState, onEvent = viewModel::onEvent)
        }

        // ========== HOME SCREEN ==========
        composable(route = Screen.Home.route) {
            HomeScreen(
                navController = navController,
                onTopAppBarStateChange = { topAppBarState.value = it },
            )
        }

        // ========== WORKOUT DETAIL SCREEN ==========
        /**
         * Detailed view of a workout session.
         * Expects [workoutId] as a String path parameter.
         */
        composable(
            route = Screen.WorkoutDetail.route,
            arguments = listOf(navArgument("workoutId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val workoutId = backStackEntry.arguments?.getString("workoutId") ?: ""
            WorkoutDetailScreen(
                workoutId = workoutId,
                navController = navController,
                onTopAppBarStateChange = { topAppBarState.value = it },
            )
        }

        // ========== ROUTINE EDITOR SCREEN ==========
        /**
         * Comprehensive editor for routine metadata and day structure.
         * Supports both New (-1) and Existing routine IDs.
         */
        composable(
            route = Screen.RoutineEditor.route,
            arguments = listOf(
                navArgument("routineId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument("folderId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            RoutineEditorScreen(
                navController = navController,
                onTopAppBarStateChange = { topAppBarState.value = it },
            )
        }

        // ========== BODY COMPOSITION SCREEN ==========
        composable(route = Screen.BodyComposition.route) {
            val viewModel: BodyCompositionViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            val subtitleTopBar = stringResource(R.string.body_composition_subtitle)

            LaunchedEffect(Unit) {
                topAppBarState.value = TopAppBarState(
                    title = UiText.StringResource(R.string.body_composition_title),
                    subtitle = subtitleTopBar,
                    showBackButton = true,
                    onBackClick = { navController.popBackStack() },
                )
            }

            LaunchedEffect(Unit) {
                viewModel.navigationEffect.collect { effect ->
                    when (effect) {
                        BodyCompositionNavEffect.NavigateToAdd -> {
                            navController.navigate(Screen.AddBodyComposition.route)
                        }
                    }
                }
            }

            BodyCompositionScreen(
                uiState = uiState,
                onAddNewMeasurement = {
                    navController.navigate(Screen.AddBodyComposition.route)
                },
                onEntryClick = { /* TODO: Navigation to entry detail placeholder */ },
            )
        }

        // ========== ADD BODY COMPOSITION SCREEN ==========
        composable(route = Screen.AddBodyComposition.route) {
            val viewModel: AddBodyCompositionViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.navigationEffect.collect { effect ->
                    when (effect) {
                        AddBodyCompositionNavEffect.SavedSuccessfully -> {
                            navController.popBackStack()
                        }
                    }
                }
            }

            AddBodyCompositionScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onNavigateBack = { navController.popBackStack() },
            )
        }

        // ========== EXERCISE PICKER SCREEN ==========
        /**
         * Contextual picker for adding exercises to a specific routine/day.
         * Passes selected IDs back to the previous screen using [savedStateHandle].
         */
        composable(
            route = Screen.ExercisePicker.route,
            arguments = listOf(
                navArgument("routineId") { type = NavType.LongType },
                navArgument("dayOrder") { type = NavType.IntType },
            ),
        ) { backStackEntry ->
            val viewModel: ExercisePickerViewModel = hiltViewModel(backStackEntry)
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(Unit) {
                topAppBarState.value = TopAppBarState(
                    title = UiText.StringResource(R.string.add_exercise),
                    showBackButton = true,
                    onBackClick = { navController.popBackStack() },
                )
            }

            ExercisePickerScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                onConfirmSelection = { selectedIds ->
                    // Standard approach for back-passing results in Compose Navigation
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("exercise_selection_result", selectedIds)
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() },
            )
        }

        // ========== SETTINGS SCREEN ==========
        composable(route = Screen.Settings.route) {
            val viewModel: SettingsViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                topAppBarState.value = TopAppBarState(
                    title = UiText.StringResource(R.string.open_settings),
                    showBackButton = true,
                    onBackClick = { navController.popBackStack() }
                )
            }

            SettingsScreen(
                viewModel = viewModel,
            )
        }
    }
}