package com.voltfitness.app.domain.model

data class WorkoutExerciseLog(
    val logId: Long,
    val sessionId: Long,
    val routineExerciseId: Long?,
    val exerciseId: String,
    val userId: Long,
    val setOrder: Int,
    val targetReps: Int?,
    val targetWeightKg: Float?,
    val targetRpe: Float?,
    val performedReps: Int?,
    val performedWeightKg: Float?,
    val performedRpe: Float?,
    val isCompleted: Boolean
)