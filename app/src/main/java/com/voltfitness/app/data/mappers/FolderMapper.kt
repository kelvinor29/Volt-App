package com.voltfitness.app.data.mappers

import com.voltfitness.app.data.local.entities.FolderEntity
import com.voltfitness.app.data.local.relations.FolderWithRoutines
import com.voltfitness.app.domain.model.Folder
import com.voltfitness.app.domain.relations.FolderWithRoutinesDomain

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

fun FolderWithRoutines.toDomain() = FolderWithRoutinesDomain(
    folder = folder.toDomain(),
    routines = routines.map { it.toDomain() }
)
