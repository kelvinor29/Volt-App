package com.voltfitness.app.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * DTO mapping the ExerciseDB API response.
 * Used both for API calls and for parsing the preseed JSON file.
 */
@Serializable
data class RemoteExerciseDto(
    val id: String,
    val name: String,
    val bodyPart: String,
    val target: String,
    val equipment: String,
    val secondaryMuscles: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val description: String?,
    val difficulty: String?,
    val category: String?,
    val gifUrl: String? = ""
)
