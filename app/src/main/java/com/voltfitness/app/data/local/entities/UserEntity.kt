package com.voltfitness.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Central user profile and settings entity.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val name: String,
    val email: String?,
    val gender: String?,
    val birthDate: Long?,           // Epoch timestamp
    val activityLevel: String?,     // Aligned with ProfileOptions.ActivityLevel
    val goal: String?,              // Aligned with ProfileOptions.FitnessGoal
    val experienceLevel: String?,   // Aligned with ProfileOptions.ExperienceLevel
    val gymName: String?,

    @ColumnInfo(defaultValue = "(strftime('%s','now') * 1000)")
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)