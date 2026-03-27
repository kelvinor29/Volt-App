package com.voltfitness.app.data.mappers

import com.voltfitness.app.data.local.entities.WorkoutSessionEntity
import com.voltfitness.app.domain.model.WorkoutSession

fun WorkoutSessionEntity.toDomain() = WorkoutSession(
    sessionId = sessionId,
    routineId = routineId,
    dayId = dayId,
    userId = userId,
    startedAt = startedAt,
    finishedAt = finishedAt,
    notes = notes
)

fun WorkoutSession.toEntity() = WorkoutSessionEntity(
    sessionId = sessionId,
    routineId = routineId,
    dayId = dayId,
    userId = userId,
    startedAt = startedAt,
    finishedAt = finishedAt,
    notes = notes
)