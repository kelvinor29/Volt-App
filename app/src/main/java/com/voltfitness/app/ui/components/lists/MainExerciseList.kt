package com.voltfitness.app.ui.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.voltfitness.app.ui.components.cards.ExerciseSummary
import com.voltfitness.app.ui.components.cards.MainExerciseCard
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * A horizontal scrollable list of featured exercises/routines.
 *
 * Designed for dashboard usage, it uses a partial-width item strategy (70% width)
 * to hint at additional content off-screen.
 *
 * @param exercises The list of [ExerciseSummary] models to display.
 * @param onExerciseClick Callback triggered when a card's primary action is clicked.
 * @param modifier Layout modifiers for the outer container.
 */
@Composable
fun MainExerciseList(
    exercises: List<ExerciseSummary>,
    onExerciseClick: (ExerciseSummary) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = VoltSpacing.extraSmall)
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = VoltSpacing.medium),
            horizontalArrangement = Arrangement.spacedBy(VoltSpacing.medium),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                items = exercises,
                key = { it.id }
            ) { exercise ->
                MainExerciseCard(
                    exercise = exercise,
                    onStartClick = { onExerciseClick(exercise) },
                    modifier = Modifier.fillParentMaxWidth(0.7f)
                )
            }
        }
    }
}

// region Previews

@Preview(name = "Main Exercise List", showBackground = true)
@Composable
private fun MainExerciseListPreview() {
    VoltTheme {
        val mockExercises = listOf(
            ExerciseSummary(1, "Chest + Triceps", "Mid - Basic Routine", "450 kcal"),
            ExerciseSummary(2, "Back + Biceps", "Mid - Basic Routine", "425 kcal"),
            ExerciseSummary(3, "Legs + Abs", "High - Basic Routine", "500 kcal"),
        )

        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            MainExerciseList(
                exercises = mockExercises,
                onExerciseClick = { /* No-op */ },
                modifier = Modifier.padding(vertical = VoltSpacing.medium)
            )
        }
    }
}
// endregion