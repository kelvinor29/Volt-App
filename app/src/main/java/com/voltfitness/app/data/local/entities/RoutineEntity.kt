package com.voltfitness.app.data.local.entities

import androidx.room.ColumnInfo
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
    @ColumnInfo(name = "is_active") val isActive: Boolean = false,
    @ColumnInfo(name = "days_per_week") val daysPerWeek: Int?,
    val createdAt: Long?,
    val updatedAt: Long?
)
