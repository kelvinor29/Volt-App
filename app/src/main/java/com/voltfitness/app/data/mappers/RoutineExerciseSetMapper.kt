package com.voltfitness.app.data.mappers

import com.voltfitness.app.data.local.entities.RoutineExerciseSetEntity
import com.voltfitness.app.domain.model.RoutineExerciseSet

fun RoutineExerciseSetEntity.toDomain() = RoutineExerciseSet(
    setId = setId,
    routineExerciseId = routineExerciseId,
    setOrder = setOrder,
    targetReps = targetReps,
    targetWeightKg = targetWeightKg,
    targetRpe = targetRpe,
    restSeconds = restSeconds
)

fun RoutineExerciseSet.toEntity() = RoutineExerciseSetEntity(
    setId = setId,
    routineExerciseId = routineExerciseId,
    setOrder = setOrder,
    targetReps = targetReps,
    targetWeightKg = targetWeightKg,
    targetRpe = targetRpe,
    restSeconds = restSeconds
)