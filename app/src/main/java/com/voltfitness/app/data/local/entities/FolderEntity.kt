package com.voltfitness.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Logical container for organizing [RoutineEntity] objects.
 */
@Entity(
    tableName = "folders",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class FolderEntity(
    @PrimaryKey(autoGenerate = true)
    val folderId: Long = 0,
    val userId: Long,
    val name: String,
    val description: String?,
    val colorHex: String?,

    @ColumnInfo(defaultValue = "(strftime('%s','now') * 1000)")
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)