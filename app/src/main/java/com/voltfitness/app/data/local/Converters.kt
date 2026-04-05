package com.voltfitness.app.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Type converters for Room database to handle complex data types.
 *
 * Utilizes Kotlinx Serialization to maintain consistency with the network layer.
 * All conversions are thread-safe and optimized for JSON string persistence.
 */
class Converters {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    /**
     * Converts a list of strings into a single JSON string for database storage.
     */
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { json.encodeToString(it) }
    }

    /**
     * Decodes a JSON string back into a list of strings.
     *
     * @return An empty list if the input string is null or blank.
     */
    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
}