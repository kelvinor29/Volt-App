package com.voltfitness.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a specific training day within a routine (e.g., "Leg Day").
 */
@Entity(
    tableName = "routine_days",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["routineId"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("routineId")]
)
data class RoutineDayEntity(
    @PrimaryKey(autoGenerate = true)
    val dayId: Long = 0,
    val routineId: Long,
    val dayOrder: Int,
    val name: String,
    val focusBodyParts: String? // Comma-separated or JSON string for quick UI display
)