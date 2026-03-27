package com.voltfitness.app.data.mappers

import com.voltfitness.app.data.local.entities.WorkoutExerciseLogEntity
import com.voltfitness.app.domain.model.WorkoutExerciseLog

fun WorkoutExerciseLogEntity.toDomain() = WorkoutExerciseLog(
    logId = logId,
    sessionId = sessionId,
    routineExerciseId = routineExerciseId,
    exerciseId = exerciseId,
    userId = userId,
    setOrder = setOrder,
    targetReps = targetReps,
    targetWeightKg = targetWeightKg,
    targetRpe = targetRpe,
    performedReps = performedReps,
    performedWeightKg = performedWeightKg,
    performedRpe = performedRpe,
    isCompleted = isCompleted
)

fun WorkoutExerciseLog.toEntity() = WorkoutExerciseLogEntity(
    logId = logId,
    sessionId = sessionId,
    routineExerciseId = routineExerciseId,
    exerciseId = exerciseId,
    userId = userId,
    setOrder = setOrder,
    targetReps = targetReps,
    targetWeightKg = targetWeightKg,
    targetRpe = targetRpe,
    performedReps = performedReps,
    performedWeightKg = performedWeightKg,
    performedRpe = performedRpe,
    isCompleted = isCompleted
)
