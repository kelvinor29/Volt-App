package com.voltfitness.app.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing the schema of the ExerciseDB API.
 *
 * Serves as the primary model for network responses and the pre-populated
 * exercise database (pre-seed JSON). Attributes are mapped to match
 * the external API provider's naming conventions.
 *
 * @property id Unique identifier provided by the API.
 * @property name The exercise title.
 * @property bodyPart Primary body region targeted (e.g., "waist", "chest").
 * @property target Specific muscle group worked (e.g., "abs", "lats").
 * @property equipment Required tools (e.g., "dumbbell", "body weight").
 * @property secondaryMuscles List of supporting muscles involved in the movement.
 * @property instructions Sequential steps to perform the exercise correctly.
 * @property description Optional textual context or tips.
 * @property difficulty Exercise complexity level.
 * @property category General grouping of the movement pattern.
 * @property gifUrl Remote link to the exercise demonstration asset.
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