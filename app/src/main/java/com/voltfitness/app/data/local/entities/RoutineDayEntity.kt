package com.voltfitness.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
    val dayOrder: Int,              // 1, 2, 3...
    val name: String,               // "Day 1 - Chest & Triceps
    val focusBodyParts: String?     // CSV/JSON: "Chest, Triceps"
)
