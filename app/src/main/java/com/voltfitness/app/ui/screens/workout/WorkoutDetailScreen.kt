package com.voltfitness.app.ui.screens.workout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.voltfitness.app.R
import com.voltfitness.app.ui.common.UiText
import com.voltfitness.app.ui.navigation.TopAppBarState
import com.voltfitness.app.ui.theme.VoltSpacing

@Composable
fun WorkoutDetailScreen(
    workoutId: String,
    navController: NavController,
    onTopAppBarStateChange: (TopAppBarState) -> Unit
) {
    LaunchedEffect(Unit) {

        val title = UiText.StringResource(R.string.workout_details)
        onTopAppBarStateChange(
            TopAppBarState(
                title = title,
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        )
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = { /* Iniciar cronómetro/entrenamiento */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(VoltSpacing.medium),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Start Session Now")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = VoltSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(VoltSpacing.medium)
        ) {
            item {
                Text(
                    text = "Exercises in this routine",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = VoltSpacing.small)
                )
            }

            items(5) { index ->
                ExerciseSetCard(name = "Exercise #$index", info = "4 Sets x 12 Reps")
            }
        }
    }
}

@Composable
fun ExerciseSetCard(name: String, info: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(VoltSpacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = name, style = MaterialTheme.typography.bodyLarge)
                Text(text = info, style = MaterialTheme.typography.bodySmall)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

    }
}

// region Previews

/**
 * Professional Preview for the Workout Detail Screen.
 * Demonstrates the list of exercises and the sticky "Start Session" button.
 */
@androidx.compose.ui.tooling.preview.Preview(
    name = "Workout Detail View",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun WorkoutDetailScreenPreview() {
    com.voltfitness.app.ui.theme.VoltTheme {
        // Surface handles the background color from our theme
        Surface(color = MaterialTheme.colorScheme.background) {
            WorkoutDetailScreen(
                workoutId = "sample_id",
                navController = androidx.navigation.compose.rememberNavController(),
                onTopAppBarStateChange = {}
            )
        }
    }
}

// endregion
