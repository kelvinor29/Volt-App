package com.voltfitness.app.core.common

/**
 * Hardcoded option lists for profile dropdown selectors.
 * These values must stay in sync with the domain layer expectations
 * (e.g. [EvaluateProgressUseCase] matches on goal strings).
 */
object ProfileOptions {
    val genders = listOf("Male", "Female")
    val activityLevels = listOf("Sedentary", "Moderate", "High")
    val goals = listOf("Hypertrophy", "Fat loss", "Recomposition", "Maintenance")
    val experienceLevels = listOf("Beginner", "Intermediate", "Advanced")
}