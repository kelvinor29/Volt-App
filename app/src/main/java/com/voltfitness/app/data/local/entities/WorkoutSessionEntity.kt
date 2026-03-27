package com.voltfitness.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_sessions",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["routineId"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = RoutineDayEntity::class,
            parentColumns = ["dayId"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("routineId"), Index("dayId"), Index("userId")]
)
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Long = 0,
    val routineId: Long?,
    val dayId: Long?,
    val userId: Long,
    val startedAt: Long,
    val finishedAt: Long?,
    val notes: String?
)
