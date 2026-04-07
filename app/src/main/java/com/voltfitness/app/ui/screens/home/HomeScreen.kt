package com.voltfitness.app.ui.screens.home

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.voltfitness.app.ui.components.cards.ExerciseSummary
import com.voltfitness.app.ui.components.cards.RoutineSummary
import com.voltfitness.app.ui.components.cards.WeightCard
import com.voltfitness.app.ui.components.lists.MainExerciseList
import com.voltfitness.app.ui.components.lists.RoutineSection
import com.voltfitness.app.ui.navigation.Screen
import com.voltfitness.app.ui.navigation.TopAppBarState
import com.voltfitness.app.ui.theme.VoltSpacing

/**
 * UI model representing a routine folder for display purposes.
 */
data class RoutineFolder(
    val id: Long,
    val name: String,
    val routines: List<RoutineSummary>
)

/**
 * Primary dashboard screen. Orchestrates weight tracking, suggested workouts,
 * and routine organization.
 *
 * @param navController Controller for cross-screen transitions.
 * @param onTopAppBarStateChange Syncs dashboard identity with the global scaffold.
 */
@Composable
fun HomeScreen(
    navController: NavController,
    onTopAppBarStateChange: (TopAppBarState) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Sync TopAppBar state based on user profile availability
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
        verticalArrangement = Arrangement.spacedBy(VoltSpacing.medium),
        contentPadding = PaddingValues(bottom = VoltSpacing.large)
    ) {
        // Summary Header: Weight status and recommended exercises
        item {
            UserDashboardHeader(
                uiState = uiState,
                onWeightClick = { navController.navigate(Screen.BodyComposition.route) },
                onExerciseClick = { exercise ->
                    navController.navigate(Screen.WorkoutDetail.createRoute(exercise.id))
                }
            )
        }

        // Routine Collections: Mapped from domain relations to UI models
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
                    navController.navigate(Screen.RoutineEditor.createRoute(routineId = routine.id))
                },
                onOptionsClick = { /* TODO: Implementation for routine management menu */ },
                onAddRoutineClick = { folderId ->
                    navController.navigate(Screen.RoutineEditor.createRoute(folderId = folderId))
                }
            )
        }
    }
}

/**
 * Composite header for the dashboard.
 * Groups the [WeightCard] with the [MainExerciseList] using semantic spacing.
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
        verticalArrangement = Arrangement.spacedBy(VoltSpacing.small)
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