package com.voltfitness.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val name: String,
    val email: String?,
    val gender: String?,
    val birthDate: Long?,            // Timestamp
    val activityLevel: String?,      // "sedentary","moderate","high"
    val goal: String?,               // "Hypertrophy","Fat loss", etc.
    val experienceLevel: String?,    // "beginner","intermediate","advanced"
    val gymName: String?,

    @androidx.room.ColumnInfo(defaultValue = "(strftime('%s','now') * 1000)")
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long?
)
