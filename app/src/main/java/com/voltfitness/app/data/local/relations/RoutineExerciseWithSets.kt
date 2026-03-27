package com.voltfitness.app.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.voltfitness.app.data.local.entities.RoutineDayEntity
import com.voltfitness.app.data.local.entities.RoutineExerciseEntity
import com.voltfitness.app.data.local.entities.RoutineExerciseSetEntity

data class RoutineExerciseWithSets(
    @Embedded
    val exercise: RoutineExerciseEntity,
    @Relation(
        parentColumn = "routineExerciseId",
        entityColumn = "routineExerciseId"
    )
    val sets: List<RoutineExerciseSetEntity>
)

data class RoutineDayFull(
    @Embedded
    val day: RoutineDayEntity,
    @Relation(
        entity = RoutineExerciseEntity::class,
        parentColumn = "dayId",
        entityColumn = "dayId"
    )
    val exercisesWithSets: List<RoutineExerciseWithSets>
)