package com.voltfitness.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Historical record of a user's physical metrics and body composition.
 *
 * Persists both raw measurements (height, weight) and derived metrics (BMI, FFMI)
 * for progress tracking over time.
 */
@Entity(
    tableName = "body_composition_entries",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("userId"),
        Index("dateEpochDay")
    ]
)
data class BodyCompositionEntity(
    @PrimaryKey(autoGenerate = true)
    val entryId: Long = 0,
    val userId: Long,
    val dateEpochDay: Long,

    // Core Metrics
    val heightCm: Float,
    val weightKg: Float,
    val bodyFatPercent: Float?,
    val waterPercent: Float?,
    val muscleMassKg: Float?,
    val visceralFatPercent: Float?,
    val basalCalories: Int?,
    val metabolicAge: Int?,
    val boneMassKg: Float?,

    // Circumferences
    val chestCm: Float?,
    val waistCm: Float?,
    val hipCm: Float?,
    val gluteCm: Float?,
    val leftArmCm: Float?,
    val rightArmCm: Float?,
    val leftLegCm: Float?,
    val rightLegCm: Float?,

    // Calculated Health Markers
    val fatMassKg: Float?,
    val leanMassKg: Float?,
    val ffmi: Float?,
    val waistHipRatio: Float?,
    val compositionScore: Float?,

    @ColumnInfo(defaultValue = "(strftime('%s','now') * 1000)")
    val createdAt: Long = System.currentTimeMillis()
)