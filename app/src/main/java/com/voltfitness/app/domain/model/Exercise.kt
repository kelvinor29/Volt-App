package com.voltfitness.app.domain.model

/**
 * Domain representation of an exercise.
 *
 * @property id Unique exercise ID from ExerciseDB (e.g., "0001").
 * @property name Display name (e.g., "3/4 sit-up").
 * @property bodyPart General body region (e.g., "waist").
 * @property target Primary muscle targeted (e.g., "abs").
 * @property equipment Required equipment (e.g., "body weight").
 * @property secondaryMuscles Additional muscles engaged.
 * @property instructions Step-by-step execution guide.
 * @property description Detailed description of the exercise.
 * @property difficulty Exercise difficulty level (e.g., "beginner").
 * @property category Exercise category (e.g., "strength").
 * @property gifUrl URL to a GIF representing the exercise.
 */
data class Exercise(
    val id: String,
    val name: String,
    val bodyPart: String,
    val target: String,
    val equipment: String,
    val secondaryMuscles: List<String>,
    val instructions: List<String>,
    val description: String?,
    val difficulty: String?,
    val category: String?,
    val gifUrl: String?
)
