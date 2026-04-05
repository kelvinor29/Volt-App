package com.voltfitness.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Local cache for API filter categories (body parts, muscle groups, equipment).
 *
 * Populating this table allows the UI to show filter options instantly
 * without requiring active network calls during navigation.
 */
@Entity(tableName = "exercise_catalogs")
data class ExerciseCatalogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val type: String, // Value must match "body_part", "target", or "equipment"
    val value: String
)