package com.voltfitness.app.data.mappers

import com.voltfitness.app.data.local.entities.RoutineEntity
import com.voltfitness.app.domain.model.Routine

fun RoutineEntity.toDomain() = Routine(
    id = routineId,
    folderId = folderId,
    name = name,
    description = description,
    goal = goal,
    isActive = isActive,
    daysPerWeek = daysPerWeek,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Routine.toEntity() = RoutineEntity(
    routineId = id,
    folderId = folderId,
    name = name,
    description = description,
    goal = goal,
    isActive = isActive,
    daysPerWeek = daysPerWeek,
    createdAt = createdAt,
    updatedAt = updatedAt
)
