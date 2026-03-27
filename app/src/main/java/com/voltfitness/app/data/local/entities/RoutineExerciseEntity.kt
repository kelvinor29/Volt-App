package com.voltfitness.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "routine_exercises",
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
data class RoutineExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val routineExerciseId: Long = 0,
    val routineId: Long,
    val dayId: Long,
    val exerciseId: String,             // Logic FK to ExerciseEntity.exerciseId
    val orderInDay: Int,
    val notes: String?,
)
