package com.voltfitness.app.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.voltfitness.app.data.local.entities.RoutineDayEntity
import com.voltfitness.app.data.local.entities.RoutineEntity

data class RoutineWithDays(
    @Embedded
    val routine: RoutineEntity,
    @Relation(
        parentColumn = "routineId",
        entityColumn = "routineId"
    )
    val days: List<RoutineDayEntity>
)