package com.voltfitness.app.domain.model

data class Folder(
    val id: Long,
    val userId: Long,
    val name: String,
    val description: String?,
    val colorHex: String?,
    val createdAt: Long,
    val updatedAt: Long?
)