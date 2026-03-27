package com.voltfitness.app.ui.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.ui.components.cards.ExerciseSummary
import com.voltfitness.app.ui.components.cards.MainExerciseCard
import com.voltfitness.app.ui.theme.VoltTheme

@Composable
fun MainExerciseList(
    exercises: List<ExerciseSummary>,
    onExerciseClick: (ExerciseSummary) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {

        // --- HORIZONTAL SCROLLABLE LIST ---
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = modifier.fillMaxWidth()
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

@Preview(name = "Main Exercise List")
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
                onExerciseClick = { exercise -> println("Clicked: ${exercise.name}") },
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}
// endregion