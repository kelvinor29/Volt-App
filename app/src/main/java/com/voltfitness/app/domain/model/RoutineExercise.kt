package com.voltfitness.app.domain.model

data class RoutineExercise(
    val routineExerciseId: Long = 0,
    val routineId: Long,
    val dayId: Long,
    val exerciseId: String,
    val orderInDay: Int,
    val notes: String?
)