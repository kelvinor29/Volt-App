package com.voltfitness.app.data.mappers

import com.voltfitness.app.data.local.entities.FolderEntity
import com.voltfitness.app.domain.model.Folder

fun FolderEntity.toDomain() = Folder(
    id = folderId,
    userId = userId,
    name = name,
    description = description,
    colorHex = colorHex,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Folder.toEntity() = FolderEntity(
    folderId = id,
    userId = userId,
    name = name,
    description = description,
    colorHex = colorHex,
    createdAt = createdAt,
    updatedAt = updatedAt
)
