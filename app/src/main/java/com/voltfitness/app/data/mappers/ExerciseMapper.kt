package com.voltfitness.app.data.mappers

import com.voltfitness.app.data.local.entities.ExerciseEntity
import com.voltfitness.app.domain.model.Exercise
import com.voltfitness.app.data.remote.dto.RemoteExerciseDto

fun RemoteExerciseDto.toEntity(): ExerciseEntity =
    ExerciseEntity(
        exerciseId = id,
        name = name,
        bodyPart = bodyPart,
        target = target,
        equipment = equipment,
        secondaryMuscles = secondaryMuscles,
        instructions = instructions,
        description = description,
        difficulty = difficulty,
        category = category,
    )

fun ExerciseEntity.toDomain(): Exercise =
    Exercise(
        id = exerciseId,
        name = name,
        bodyPart = bodyPart,
        target = target,
        equipment = equipment,
        secondaryMuscles = secondaryMuscles,
        instructions = instructions,
        description = description,
        difficulty = difficulty,
        category = category,
        gifUrl = null
    )