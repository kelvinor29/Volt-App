package com.voltfitness.app.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.voltfitness.app.data.local.entities.RoutineDayEntity
import com.voltfitness.app.data.local.entities.RoutineEntity
import com.voltfitness.app.data.local.entities.RoutineExerciseEntity

/**
 * Aggregate data class that represents a complete routine hierarchy,
 * including its days and nested exercises with their sets.
 */
data class RoutineWithFullDays(
    @Embedded val routine: RoutineEntity,
    @Relation(
        entity = RoutineDayEntity::class,
        parentColumn = "routineId",
        entityColumn = "routineId"
    )
    val days: List<RoutineDayWithExercises>
)

/**
 * Represents a specific day within a routine and its associated exercises.
 */
data class RoutineDayWithExercises(
    @Embedded val day: RoutineDayEntity,
    @Relation(
        entity = RoutineExerciseEntity::class,
        parentColumn = "dayId",
        entityColumn = "dayId"
    )
    val exercises: List<RoutineExerciseWithSets>
)
