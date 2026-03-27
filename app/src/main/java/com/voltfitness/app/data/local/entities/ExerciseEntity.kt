package com.voltfitness.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a cached exercise from the ExerciseDB API.
 * Includes all fields from the API response for offline-first access.
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
