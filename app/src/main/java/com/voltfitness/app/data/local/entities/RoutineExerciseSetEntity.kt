package com.voltfitness.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Prescribed targets for a specific set within a routine exercise.
 */
@Entity(
    tableName = "routine_exercises_sets",
    foreignKeys = [
        ForeignKey(
            entity = RoutineExerciseEntity::class,
            parentColumns = ["routineExerciseId"],
            childColumns = ["routineExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("routineExerciseId")]
)
data class RoutineExerciseSetEntity(
    @PrimaryKey(autoGenerate = true)
    val setId: Long = 0,
    val routineExerciseId: Long,
    val setOrder: Int,
    val targetReps: Int?,
    val targetWeightKg: Float?,
    val targetRpe: Float?,
    val restSeconds: Int?,
)