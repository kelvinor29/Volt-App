package com.voltfitness.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Local representation of a fitness exercise.
 *
 * Synchronized with the ExerciseDB API to support full offline capabilities
 * including exercise search and instruction reading.
 */
@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey
    val exerciseId: String,
    val name: String,
    val bodyPart: String,
    val target: String,
    val equipment: String,
    val secondaryMuscles: List<String>,
    val instructions: List<String>,
    val description: String?,
    val difficulty: String?,
    val category: String?
)