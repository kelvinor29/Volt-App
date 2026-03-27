package com.voltfitness.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "workout_exercise_logs",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSessionEntity::class,
            parentColumns = ["sessionId"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("sessionId"), Index("exerciseId"), Index("routineExerciseId")]
)
data class WorkoutExerciseLogEntity(
    @PrimaryKey(autoGenerate = true)
    val logId: Long = 0,
    val sessionId: Long,
    val routineExerciseId: Long?,
    val exerciseId: String,
    val userId: Long,
    val setOrder: Int,
    val targetReps: Int?,
    val targetWeightKg: Float?,
    val targetRpe: Float?,
    val performedReps: Int?,
    val performedWeightKg: Float?,
    val performedRpe: Float?,
    val isCompleted: Boolean
)
