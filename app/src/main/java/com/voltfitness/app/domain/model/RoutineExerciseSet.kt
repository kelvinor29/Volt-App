package com.voltfitness.app.domain.model

data class RoutineExerciseSet(
    val setId: Long,
    val routineExerciseId: Long,
    val setOrder: Int,
    val targetReps: Int?,
    val targetWeightKg: Float?,
    val targetRpe: Float?,
    val restSeconds: Int?
)