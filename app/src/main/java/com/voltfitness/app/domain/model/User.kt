package com.voltfitness.app.domain.model

data class User(
    val id: Long,
    val name: String,
    val email: String?,
    val gender: String?,
    val birthDate: Long?,
    val activityLevel: String?,
    val goal: String?,
    val experienceLevel: String?,
    val gymName: String?,
    val createdAt: Long,
    val updatedAt: Long?
)