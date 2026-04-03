package com.voltfitness.app.domain.model

data class Routine(
    val id: Long,
    val folderId: Long,
    val name: String,
    val description: String?,
    val goal: String?,
    val isActive: Boolean,
    val daysPerWeek: Int?,
    val createdAt: Long?,
    val updatedAt: Long?
)