package com.voltfitness.app.data.mappers

import com.voltfitness.app.data.local.entities.RoutineExerciseEntity
import com.voltfitness.app.domain.model.RoutineExercise

fun RoutineExerciseEntity.toDomain() = RoutineExercise(
    routineId = routineId,
    dayId = dayId,
    exerciseId = exerciseId,
    orderInDay = orderInDay,
    notes = notes,
    routineExerciseId = routineExerciseId
)

fun RoutineExercise.toEntity() = RoutineExerciseEntity(
    routineExerciseId = routineExerciseId,
    dayId = dayId,
    exerciseId = exerciseId,
    orderInDay = orderInDay,
    notes = notes,
    routineId = routineId
)