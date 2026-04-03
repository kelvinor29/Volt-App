package com.voltfitness.app.data.mappers

import com.voltfitness.app.data.local.entities.RoutineExerciseEntity
import com.voltfitness.app.domain.model.RoutineExercise

/**
 * Maps between [RoutineExerciseEntity] (data layer) and [RoutineExercise] (domain layer).
 *
 * [RoutineExerciseEntity.exerciseName] is denormalized in the entity so the
 * domain model always has a readable name without an extra join to the exercises table.
 */
fun RoutineExerciseEntity.toDomain() = RoutineExercise(
    routineExerciseId = routineExerciseId,
    routineId = routineId,
    dayId = dayId,
    exerciseId = exerciseId,
    exerciseName = exerciseName,
    orderInDay = orderInDay,
    sets = sets,
    repsRange = repsRange,
    weightRange = weightRange,
    notes = notes
)

fun RoutineExercise.toEntity() = RoutineExerciseEntity(
    routineExerciseId = routineExerciseId,
    routineId = routineId,
    dayId = dayId,
    exerciseId = exerciseId,
    exerciseName = exerciseName,
    orderInDay = orderInDay,
    sets = sets,
    repsRange = repsRange,
    weightRange = weightRange,
    notes = notes
)