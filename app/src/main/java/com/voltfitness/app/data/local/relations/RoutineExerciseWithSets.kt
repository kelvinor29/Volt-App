package com.voltfitness.app.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.voltfitness.app.data.local.entities.RoutineExerciseEntity
import com.voltfitness.app.data.local.entities.RoutineExerciseSetEntity

/**
 * Room relation representing one routine exercise with all its planned sets.
 */
data class RoutineExerciseWithSets(
    @Embedded val exercise: RoutineExerciseEntity,
    @Relation(
        parentColumn = "routineExerciseId",
        entityColumn = "routineExerciseId"
    )
    val sets: List<RoutineExerciseSetEntity>
)