package com.voltfitness.app.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
    indices = [Index("userId"), Index("dateEpochDay")]
)
data class BodyCompositionEntity(
    @PrimaryKey(autoGenerate = true)
    val entryId: Long = 0,
    val userId: Long,
    val dateEpochDay: Long,       // LocalDate.toEpochDay()

    // Body composition data
    val heightCm: Float,
    val weightKg: Float,
    val bodyFatPercent: Float?,
    val waterPercent: Float?,
    val muscleMassKg: Float?,
    val visceralFatPercent: Float?,
    val basalCalories: Int?,
    val metabolicAge: Int?,
    val boneMassKg: Float?,

    // Body measurements
    val chestCm: Float?,
    val waistCm: Float?,
    val hipCm: Float?,
    val gluteCm: Float?,
    val leftArmCm: Float?,
    val rightArmCm: Float?,
    val leftLegCm: Float?,
    val rightLegCm: Float?,

    // Calculated data
    val fatMassKg: Float?,          // Weight x (BFP / 100)
    val leanMassKg: Float?,         // Weight - Fat mass
    val ffmi: Float?,               // FFM / height_m2
    val waistHipRatio: Float?,      // Waist / Hip
    val compositionScore: Float?,

    @androidx.room.ColumnInfo(defaultValue = "(strftime('%s','now') * 1000)")
    val createdAt: Long = System.currentTimeMillis(),
)

