package com.voltfitness.app.core.common

/**
 * Single source of truth for user profile attributes.
 *
 * This provider uses Enums to decouple UI display strings from domain logic values,
 * preventing business logic failure due to typos or localization changes.
 */
object ProfileOptions {

    /**
     * User gender options with stable identifiers for domain logic.
     */
    enum class Gender(val label: String) {
        MALE("Male"),
        FEMALE("Female")
    }

    /**
     * Physical activity frequency and intensity levels.
     */
    enum class ActivityLevel(val label: String) {
        SEDENTARY("Sedentary"),
        MODERATE("Moderate"),
        HIGH("High")
    }

    /**
     * Primary fitness objectives used by [EvaluateProgressUseCase].
     */
    enum class FitnessGoal(val label: String) {
        HYPERTROPHY("Hypertrophy"),
        FAT_LOSS("Fat loss"),
        RECOMPOSITION("Recomposition"),
        MAINTENANCE("Maintenance")
    }

    /**
     * User's technical proficiency in resistance training.
     */
    enum class ExperienceLevel(val label: String) {
        BEGINNER("Beginner"),
        INTERMEDIATE("Intermediate"),
        ADVANCED("Advanced")
    }

    /**
     * String-based lists for UI components (e.g., dropdowns).
     * Derived directly from Enums to ensure synchronization.
     */
    val genders = Gender.entries.map { it.label }
    val activityLevels = ActivityLevel.entries.map { it.label }
    val goals = FitnessGoal.entries.map { it.label }
    val experienceLevels = ExperienceLevel.entries.map { it.label }
}