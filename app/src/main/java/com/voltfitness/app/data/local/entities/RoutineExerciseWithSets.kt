package com.voltfitness.app.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Room relation: one [RoutineExerciseEntity] → its [RoutineExerciseSetEntity] list.
 * Pure data layer — no domain logic here.
 */
data class RoutineExerciseWithSets(
    @Embedded val exercise: RoutineExerciseEntity,
    @Relation(
        parentColumn = "routineExerciseId",
        entityColumn = "routineExerciseId"
    )
    val sets: List<RoutineExerciseSetEntity>
)