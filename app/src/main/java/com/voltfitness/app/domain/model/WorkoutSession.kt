package com.voltfitness.app.domain.model

data class WorkoutSession(
    val sessionId: Long,
    val routineId: Long?,
    val dayId: Long?,
    val userId: Long,
    val startedAt: Long,
    val finishedAt: Long?,
    val notes: String?
)