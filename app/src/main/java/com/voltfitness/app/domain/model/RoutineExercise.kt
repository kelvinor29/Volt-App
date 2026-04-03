package com.voltfitness.app.domain.model

/**
 * Domain model representing an exercise assigned to a routine day.
 *
 * [exerciseId] references the external ExerciseDB identifier.
 * [exerciseName] is denormalized so the routine remains readable even if
 * the external catalog changes or is temporarily unavailable.
 */
data class RoutineExercise(
    val routineExerciseId: Long = 0L,
    val routineId: Long,
    val dayId: Long,
    val exerciseId: String,
    val exerciseName: String,
    val orderInDay: Int,
    val sets: Int,
    val repsRange: String,
    val weightRange: String,
    val notes: String?
)