package com.voltfitness.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Local entity representing an exercise assigned to a specific routine day.
 *
 * [exerciseName] is intentionally stored here to avoid depending on a join
 * with the exercise catalog every time the routine detail/editor is loaded.
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