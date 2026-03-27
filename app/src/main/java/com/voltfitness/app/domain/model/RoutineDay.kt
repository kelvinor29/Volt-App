package com.voltfitness.app.domain.model

/**
 * Domain representation of a training day within a routine.
 *
 * @property id Unique identifier (0 for new/unsaved days).
 * @property routineId The parent routine this day belongs to.
 * @property dayOrder Position in the routine (1-based).
 * @property name Display name (e.g., "Day 1 - Chest & Triceps").
 * @property focusBodyParts Optional CSV of target muscle groups.
 */
data class RoutineDay(
    val id: Long = 0L,
    val routineId: Long,
    val dayOrder: Int,
    val name: String,
    val focusBodyParts: String? = null
)
