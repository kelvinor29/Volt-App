package com.voltfitness.app.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.voltfitness.app.data.local.entities.FolderEntity
import com.voltfitness.app.data.local.entities.RoutineEntity

/**
 * Represents a one-to-many relationship between a folder and its contained routines.
 */
data class FolderWithRoutines(
    @Embedded
    val folder: FolderEntity,
    @Relation(
        parentColumn = "folderId",
        entityColumn = "folderId"
    )
    val routines: List<RoutineEntity>
)