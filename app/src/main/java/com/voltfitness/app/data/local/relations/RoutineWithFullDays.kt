package com.voltfitness.app.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.voltfitness.app.data.local.entities.RoutineDayEntity
import com.voltfitness.app.data.local.entities.RoutineEntity

/**
 * Full Room aggregate for a routine:
 * routine metadata -> days -> exercises with sets.
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
 * Room relation representing one day and all its exercises.
 */
data class RoutineDayWithExercises(
    @Embedded val day: RoutineDayEntity,
    @Relation(
        entity = com.voltfitness.app.data.local.entities.RoutineExerciseEntity::class,
        parentColumn = "dayId",
        entityColumn = "dayId"
    )
    val exercises: List<RoutineExerciseWithSets>
)