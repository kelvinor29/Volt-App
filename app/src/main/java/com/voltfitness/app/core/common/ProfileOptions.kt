package com.voltfitness.app.core.common

import androidx.annotation.StringRes
import com.voltfitness.app.R

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
    enum class Gender(val dbValue: String, @StringRes val labelRes: Int) {
        MALE("Male", R.string.gender_male),
        FEMALE("Female", R.string.gender_female)
    }

    /**
     * Physical activity frequency and intensity levels.
     */
    enum class ActivityLevel(val dbValue: String, @StringRes val labelRes: Int) {
        SEDENTARY("Sedentary", R.string.activity_sedentary),
        MODERATE("Moderate", R.string.activity_moderate),
        HIGH("High", R.string.activity_high)
    }

    /**
     * Primary fitness objectives used by [EvaluateProgressUseCase].
     */
    enum class FitnessGoal(val dbValue: String, @StringRes val labelRes: Int) {
        HYPERTROPHY("Hypertrophy", R.string.goal_hypertrophy),
        FAT_LOSS("Fat loss", R.string.goal_fat_loss),
        RECOMPOSITION("Recomposition", R.string.goal_recomposition),
        MAINTENANCE("Maintenance", R.string.goal_maintenance)
    }

    /**
     * User's technical proficiency in resistance training.
     */
    enum class ExperienceLevel(val dbValue: String, @StringRes val labelRes: Int) {
        BEGINNER("Beginner", R.string.exp_beginner),
        INTERMEDIATE("Intermediate", R.string.exp_intermediate),
        ADVANCED("Advanced", R.string.exp_advanced)
    }
}