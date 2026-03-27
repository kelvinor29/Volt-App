package com.voltfitness.app.data.mappers

import com.voltfitness.app.data.local.entities.RoutineDayEntity
import com.voltfitness.app.domain.model.RoutineDay

// RoutineDayEntity → RoutineDay
fun RoutineDayEntity.toDomain() = RoutineDay(
    id = dayId,
    routineId = routineId,
    dayOrder = dayOrder,
    name = name,
    focusBodyParts = focusBodyParts
)

// RoutineDay → RoutineDayEntity
fun RoutineDay.toEntity() = RoutineDayEntity(
    dayId = id,
    routineId = routineId,
    dayOrder = dayOrder,
    name = name,
    focusBodyParts = focusBodyParts
)

