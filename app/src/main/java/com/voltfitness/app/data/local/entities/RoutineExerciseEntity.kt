package com.voltfitness.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Mapping between a routine day and a specific exercise.
 *
 * [exerciseName] is denormalized here to allow quick rendering of routine lists
 * without heavy JOIN operations on the exercise catalog.
 */
@Entity(
    tableName = "routine_exercises",
    foreignKeys = [
        ForeignKey(
            entity = RoutineDayEntity::class,
            parentColumns = ["dayId"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("routineId"),
        Index("dayId")
    ]
)
data class RoutineExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val routineExerciseId: Long = 0L,
    val routineId: Long,
    val dayId: Long,
    val exerciseId: String,

    @ColumnInfo(name = "exercise_name")
    val exerciseName: String,

    val orderInDay: Int,
    val sets: Int,
    val repsRange: String,
    val weightRange: String,
    val notes: String?
)