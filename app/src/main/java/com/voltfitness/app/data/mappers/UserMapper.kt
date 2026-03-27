package com.voltfitness.app.data.mappers

import com.voltfitness.app.data.local.entities.UserEntity
import com.voltfitness.app.domain.model.User

fun UserEntity.toDomain() = User(
    id = userId,
    name = name,
    email = email,
    gender = gender,
    birthDate = birthDate,
    activityLevel = activityLevel,
    goal = goal,
    experienceLevel = experienceLevel,
    gymName = gymName,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun User.toEntity() = UserEntity(
    userId = id,
    name = name,
    email = email,
    gender = gender,
    birthDate = birthDate,
    activityLevel = activityLevel,
    goal = goal,
    experienceLevel = experienceLevel,
    gymName = gymName,
    createdAt = createdAt,
    updatedAt = updatedAt
)
