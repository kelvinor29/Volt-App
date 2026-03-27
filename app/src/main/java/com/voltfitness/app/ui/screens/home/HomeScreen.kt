package com.voltfitness.app.ui.screens.home

import WeightCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.voltfitness.app.ui.components.cards.ExerciseSummary
import com.voltfitness.app.ui.components.cards.RoutineSummary
import com.voltfitness.app.ui.components.lists.MainExerciseList
import com.voltfitness.app.ui.components.lists.RoutineSection
import com.voltfitness.app.ui.navigation.Screen
import com.voltfitness.app.ui.navigation.TopAppBarState
import com.voltfitness.app.ui.theme.VoltSpacing

/**
 * Lightweight UI model for grouping routines under a named folder.
 * Maps from [FolderWithRoutinesDomain] for display in [RoutineSection].
 */
data class RoutineFolder(
    val id: Long,
    val name: String,
    val routines: List<RoutineSummary>
)

/**
 * Home screen displaying the user dashboard: weight status,
 * suggested workouts, and routine folders.
 *
 * All navigation is handled here in the UI layer via [navController].
 * The [viewModel] only exposes state — no navigation events.
 *
 * @param navController Navigation controller for screen transitions.
 * @param onTopAppBarStateChange Callback to sync the top app bar with this screen.
 * @param viewModel Hilt-injected [HomeViewModel].
 */
@Composable
fun HomeScreen(
    navController: NavController,
    onTopAppBarStateChange: (TopAppBarState) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Configure the top app bar whenever the user name changes.
    LaunchedEffect(uiState.user?.name) {
        onTopAppBarStateChange(
            TopAppBarState(
                title = "Hello, ${uiState.user?.name ?: "User"}!",
                subtitle = "Get ready, today is workout day!",
                showBackButton = false,
                showSettingsButton = true,
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Weight card + suggested workouts
        item {
            UserDashboardHeader(
                uiState = uiState,
                onWeightClick = { navController.navigate(Screen.BodyComposition.route) },
                onExerciseClick = { exercise ->
                    navController.navigate(
                        Screen.WorkoutDetail.createRoute(exercise.id)
                    )
                }
            )
        }

        // Routine folders
        item {
            RoutineSection(
                folders = uiState.folders.map { domain ->
                    RoutineFolder(
                        id = domain.folder.id,
                        name = domain.folder.name,
                        routines = domain.routines.map { routine ->
                            RoutineSummary(
                                id = routine.id,
                                name = routine.name,
                                daysCount = routine.daysPerWeek ?: 0,
                                description = routine.goal ?: "",
                                isMainRoutine = routine.isActive
                            )
                        }
                    )
                },
                onRoutineClick = { routine ->
                    navController.navigate(
                        Screen.RoutineDetail.createRoute(routine.id)
                    )
                },
                onOptionsClick = { /* TODO: Routine options menu */ },
                onAddRoutineClick = { folderId ->
                    navController.navigate(
                        Screen.RoutineCreator.createRoute(folderId)
                    )
                }
            )
        }    }
}

/**
 * Header section combining the [WeightCard] with the suggested workout list.
 * Extracted as a private composable for readability.
 */
@Composable
private fun UserDashboardHeader(
    uiState: HomeUiState,
    onWeightClick: () -> Unit,
    onExerciseClick: (ExerciseSummary) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        WeightCard(
            uiState = uiState,
            modifier = Modifier.padding(horizontal = VoltSpacing.medium),
            onClick = onWeightClick
        )

        MainExerciseList(
            exercises = uiState.suggestedWorkouts,
            onExerciseClick = onExerciseClick
        )
    }
}
