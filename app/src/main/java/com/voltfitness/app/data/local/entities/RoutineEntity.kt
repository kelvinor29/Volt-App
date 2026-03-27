package com.voltfitness.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "routines",
    foreignKeys = [
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["folderId"],
            childColumns = ["folderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("folderId")]
)
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true)
    val routineId: Long = 0,
    val folderId: Long,
    val name: String,
    val description: String?,
    val goal: String?,              // Hipertrofy, etc..
    val isActive: Boolean,
    val daysPerWeek: Int?,

    @androidx.room.ColumnInfo(defaultValue = "(strftime('%s','now') * 1000)")
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long?
)
