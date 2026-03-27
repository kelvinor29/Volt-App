package com.voltfitness.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Stores catalog values (body parts, targets, equipment) locally.
 * Uses [type] to distinguish between catalog categories.
 *
 * This avoids hitting the API every time the user opens the filter UI.
 */
@Entity(tableName = "exercise_catalogs")
data class ExerciseCatalogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val type: String,   // "body_part", "target", or "equipment"
    val value: String    // e.g., "chest", "biceps", "dumbbell"
)
