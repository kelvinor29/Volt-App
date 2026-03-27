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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.domain.model.ProgressStatus
import com.voltfitness.app.domain.model.User
import com.voltfitness.app.ui.components.cards.ExerciseSummary
import com.voltfitness.app.ui.components.cards.RoutineSummary
import com.voltfitness.app.ui.components.lists.MainExerciseList
import com.voltfitness.app.ui.components.lists.RoutineSection
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme

// ─────────────────────────────────────────
// PREVIEW DATA
// ─────────────────────────────────────────

private val previewUser = User(
    id = 1L,
    name = "John Doe",
    email = "john@volt.app",
    gender = "Male",
    birthDate = null,
    activityLevel = "Moderate",
    goal = "Hypertrophy",
    experienceLevel = "Intermediate",
    gymName = "Volt Gym",
    createdAt = System.currentTimeMillis(),
    updatedAt = null
)

private val previewExercises = listOf(
    ExerciseSummary(1, "Chest + Triceps", "Mid · Basic Routine", "450 kcal"),
    ExerciseSummary(2, "Back + Biceps", "Mid · Basic Routine", "425 kcal"),
    ExerciseSummary(3, "Legs + Abs", "High · Basic Routine", "500 kcal")
)

private val previewFolders = listOf(
    RoutineFolder(
        id = 1L,
        name = "My Custom Routines",
        routines = listOf(
            RoutineSummary(1, "Basic Routine", 8, "Chest, Shoulders, Triceps", true),
            RoutineSummary(2, "Recommended", 7, "Back, Biceps", false)
        )
    ),
    RoutineFolder(
        id = 2L,
        name = "Volt Community Programs",
        routines = listOf(
            RoutineSummary(4, "PPL", 6, "Total body strength", false),
            RoutineSummary(5, "HIIT Blast", 12, "High intensity cardio", false)
        )
    )
)

// ─────────────────────────────────────────
// PREVIEWS
// ─────────────────────────────────────────

/**
 * Full dashboard preview with weight data, exercises, and routine folders.
 * Renders the content directly (bypasses Hilt ViewModel).
 */
@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A, name = "Home – Dashboard")
@Composable
private fun HomeDashboardPreview() {
    val state = HomeUiState(
        isLoading = false,
        user = previewUser,
        currentWeight = "82.5",
        lastWeightUpdated = "Updated today",
        primaryChangeText = "Score +2.1",
        secondaryChangeText = "Fat -0.8% | Muscle +0.4 kg",
        progressStatus = ProgressStatus.POSITIVE,
        compositionScore = 88f,
        suggestedWorkouts = previewExercises
    )

    VoltTheme {
        HomeContentPreview(uiState = state)
    }
}

/**
 * Empty state preview — new user with no records.
 */
@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A, name = "Home – Empty")
@Composable
private fun HomeEmptyPreview() {
    val state = HomeUiState(
        isLoading = false,
        user = previewUser,
        currentWeight = "",
        lastWeightUpdated = "No records yet",
        primaryChangeText = "Welcome!",
        secondaryChangeText = "Log your weight to get started",
        progressStatus = ProgressStatus.NEUTRAL
    )

    VoltTheme {
        HomeContentPreview(uiState = state)
    }
}

/**
 * Stateless content renderer for previews.
 * Mirrors [HomeScreen] layout without requiring a [NavController] or ViewModel.
 */
@Composable
private fun HomeContentPreview(uiState: HomeUiState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WeightCard(
                    uiState = uiState,
                    modifier = Modifier.padding(horizontal = VoltSpacing.medium)
                )
                MainExerciseList(
                    exercises = uiState.suggestedWorkouts,
                    onExerciseClick = {}
                )
            }
        }

        item {
            RoutineSection(
                folders = previewFolders,
                onRoutineClick = {},
                onOptionsClick = {},
                modifier = Modifier.padding(horizontal = VoltSpacing.medium),
                onAddRoutineClick = {}
            )
        }
    }
}
