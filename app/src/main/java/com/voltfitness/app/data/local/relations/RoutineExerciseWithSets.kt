package com.voltfitness.app.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.voltfitness.app.data.local.entities.RoutineExerciseEntity
import com.voltfitness.app.data.local.entities.RoutineExerciseSetEntity

/**
 * Represents an exercise within a routine along with its collection of planned sets.
 */
data class RoutineExerciseWithSets(
    @Embedded val exercise: RoutineExerciseEntity,
    @Relation(
        parentColumn = "routineExerciseId",
        entityColumn = "routineExerciseId"
    )
    val sets: List<RoutineExerciseSetEntity>
)