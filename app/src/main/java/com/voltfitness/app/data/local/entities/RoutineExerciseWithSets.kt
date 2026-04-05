package com.voltfitness.app.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Helper class for Room to retrieve an exercise with all its associated sets.
 */
data class RoutineExerciseWithSets(
    @Embedded val exercise: RoutineExerciseEntity,

    @Relation(
        parentColumn = "routineExerciseId",
        entityColumn = "routineExerciseId"
    )
    val sets: List<RoutineExerciseSetEntity>
)