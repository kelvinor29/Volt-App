package com.voltfitness.app.domain.usecase.user

import com.voltfitness.app.domain.model.BodyCompositionEntry
import com.voltfitness.app.domain.model.ProgressEvaluation
import com.voltfitness.app.domain.model.ProgressStatus
import javax.inject.Inject
import kotlin.math.abs

/**
 * Evaluates a user's body composition progress by comparing the most recent
 * assessment against the previous one.
 *
 * The evaluation strategy adapts to the user's fitness goal:
 * - **Fat loss**: prioritizes body fat reduction while preserving muscle mass.
 * - **Hypertrophy**: prioritizes muscle gain with controlled fat gain.
 * - **Recomposition**: evaluates the overall composition score change.
 * - **Maintenance**: rewards stability (minimal score drift).
 * - **General / unknown**: falls back to composition score delta.
 *
 * When specific metrics (body fat %, muscle mass) are unavailable, the
 * evaluation gracefully degrades to the composition score as a fallback.
 *
 * @see com.voltfitness.app.domain.usecase.body_composition.CalculateCompositionScoreUseCase for how the composition score is derived.
 * @see ProgressStatus for the possible evaluation outcomes.
 */
class EvaluateProgressUseCase @Inject constructor() {

    /**
     * Compares [current] against [previous] entry and returns a [ProgressEvaluation]
     * indicating the user's progress toward their [userGoal].
     *
     * @param current the most recent body composition assessment.
     * @param previous the assessment immediately before [current], or `null`
     *   if this is the user's first assessment.
     * @param userGoal the user's fitness goal as stored in [UserEntity.goal],
     *   e.g. `"fat loss"`, `"hypertrophy"`, `"recomposition"`, `"maintenance"`.
     *   Case-insensitive. A `null` or unrecognized goal triggers the general
     *   evaluation strategy.
     * @return a [ProgressEvaluation] containing the [ProgressStatus], a primary
     *   display text, and an optional secondary display text.
     */
    operator fun invoke(
        current: BodyCompositionEntry,
        previous: BodyCompositionEntry?,
        userGoal: String?
    ): ProgressEvaluation {
        if (previous == null) {
            return ProgressEvaluation(
                status = ProgressStatus.NEUTRAL,
                primaryText = "First assessment",
                secondaryText = null
            )
        }

        return when (userGoal?.lowercase()) {
            "fat loss" -> evaluateFatLoss(current, previous)
            "hypertrophy" -> evaluateHypertrophy(current, previous)
            "recomposition" -> evaluateRecomposition(current, previous)
            "maintenance" -> evaluateMaintenance(current, previous)
            else -> evaluateGeneral(current, previous)
        }
    }

    /**
     * Fat loss strategy: POSITIVE when body fat drops while muscle is preserved.
     * NEGATIVE when body fat increases. Falls back to score delta when fat data
     * is unavailable.
     *
     * Thresholds:
     * - Fat change <= -0.5% = significant loss.
     * - Muscle change >= -0.3 kg = muscle preserved.
     * - Fat change >= +0.5% = undesired gain.
     */
    private fun evaluateFatLoss(
        current: BodyCompositionEntry,
        previous: BodyCompositionEntry
    ): ProgressEvaluation {
        val fatChange = diff(current.bodyFatPercent, previous.bodyFatPercent)
        val muscleChange = diff(current.muscleMassKg, previous.muscleMassKg)
        val scoreChange = diff(current.compositionScore, previous.compositionScore)
        val hasFatData = current.bodyFatPercent != null && previous.bodyFatPercent != null

        val status = when {
            hasFatData && fatChange <= -0.5f && muscleChange >= -0.3f ->
                ProgressStatus.POSITIVE
            hasFatData && fatChange <= -0.5f && muscleChange < -0.3f ->
                ProgressStatus.NEUTRAL
            hasFatData && fatChange >= 0.5f ->
                ProgressStatus.NEGATIVE
            scoreChange > 2f -> ProgressStatus.POSITIVE
            scoreChange < -2f -> ProgressStatus.NEGATIVE
            else -> ProgressStatus.NEUTRAL
        }

        return ProgressEvaluation(
            status = status,
            primaryText = formatFatChange(fatChange, hasFatData),
            secondaryText = formatMuscleChange(
                muscleChange,
                current.muscleMassKg != null && previous.muscleMassKg != null
            )
        )
    }

    /**
     * Hypertrophy strategy: POSITIVE when muscle increases with controlled fat gain.
     * NEGATIVE when muscle drops or fat gain exceeds acceptable bulk threshold.
     *
     * Thresholds:
     * - Muscle change > +0.3 kg = significant gain.
     * - Fat change < +3% = acceptable accompaniment during bulk.
     * - Fat change >= +3% = excessive fat gain.
     */
    private fun evaluateHypertrophy(
        current: BodyCompositionEntry,
        previous: BodyCompositionEntry
    ): ProgressEvaluation {
        val muscleChange = diff(current.muscleMassKg, previous.muscleMassKg)
        val fatChange = diff(current.bodyFatPercent, previous.bodyFatPercent)
        val hasMuscleData = current.muscleMassKg != null && previous.muscleMassKg != null

        val status = when {
            hasMuscleData && muscleChange > 0.3f && fatChange < 3f ->
                ProgressStatus.POSITIVE
            hasMuscleData && muscleChange < -0.3f ->
                ProgressStatus.NEGATIVE
            fatChange >= 3f -> ProgressStatus.NEGATIVE
            else -> ProgressStatus.NEUTRAL
        }

        return ProgressEvaluation(
            status = status,
            primaryText = formatMuscleChange(muscleChange, hasMuscleData),
            secondaryText = formatFatChange(
                fatChange,
                current.bodyFatPercent != null && previous.bodyFatPercent != null
            )
        )
    }

    /**
     * Recomposition strategy: evaluates the overall composition score, which
     * captures simultaneous fat loss and muscle gain.
     *
     * Thresholds:
     * - Score change > +2 = meaningful improvement.
     * - Score change < -2 = meaningful regression.
     */
    private fun evaluateRecomposition(
        current: BodyCompositionEntry,
        previous: BodyCompositionEntry
    ): ProgressEvaluation {
        val scoreChange = diff(current.compositionScore, previous.compositionScore)
        val fatChange = diff(current.bodyFatPercent, previous.bodyFatPercent)
        val muscleChange = diff(current.muscleMassKg, previous.muscleMassKg)

        val status = when {
            scoreChange > 2f -> ProgressStatus.POSITIVE
            scoreChange < -2f -> ProgressStatus.NEGATIVE
            else -> ProgressStatus.NEUTRAL
        }

        val primaryText = if (abs(scoreChange) >= 0.1f)
            "Score ${formatDelta(scoreChange)}"
         else "Score unchanged"


        val hasFat = current.bodyFatPercent != null && previous.bodyFatPercent != null
        val hasMuscle = current.muscleMassKg != null && previous.muscleMassKg != null

        val secondary = buildString {
            if (hasFat && abs(fatChange) >= 0.1f) {
                append("Fat ${formatDelta(fatChange)}%")
            }
            if (hasFat && abs(fatChange) >= 0.1f && hasMuscle && abs(muscleChange) >= 0.1f) {
                append(" | ")
            }
            if (hasMuscle && abs(muscleChange) >= 0.1f) {
                append("Muscle ${formatDelta(muscleChange)} kg")
            }
        }.ifEmpty { null }

        return ProgressEvaluation(
            status = status,
            primaryText = primaryText,
            secondaryText = secondary
        )
    }

    /**
     * Maintenance strategy: POSITIVE when the composition score remains stable
     * (drift <= 1 point). NEGATIVE only on significant decline.
     * A large positive improvement is also considered POSITIVE since the user's
     * composition is objectively better even if the goal is stability.
     *
     * Thresholds:
     * - |score change| <= 1 = stable (ideal for maintenance).
     * - score change > +1 = improved, still positive.
     * - score change < -2 = significant decline.
     */
    private fun evaluateMaintenance(
        current: BodyCompositionEntry,
        previous: BodyCompositionEntry
    ): ProgressEvaluation {
        val scoreChange = diff(current.compositionScore, previous.compositionScore)

        val status = when {
            abs(scoreChange) <= 1f -> ProgressStatus.POSITIVE
            scoreChange > 1f -> ProgressStatus.POSITIVE
            scoreChange < -2f -> ProgressStatus.NEGATIVE
            else -> ProgressStatus.NEUTRAL // between -2 and -1
        }

        return ProgressEvaluation(
            status = status,
            primaryText = "Score ${formatDelta(scoreChange)}",
            secondaryText = null
        )
    }

    /**
     * General fallback strategy when the user has no specific goal set.
     * Relies entirely on the composition score delta.
     */
    private fun evaluateGeneral(
        current: BodyCompositionEntry,
        previous: BodyCompositionEntry
    ): ProgressEvaluation {
        val scoreChange = diff(current.compositionScore, previous.compositionScore)

        val status = when {
            scoreChange > 2f -> ProgressStatus.POSITIVE
            scoreChange < -2f -> ProgressStatus.NEGATIVE
            else -> ProgressStatus.NEUTRAL
        }

        return ProgressEvaluation(
            status = status,
            primaryText = "Score ${formatDelta(scoreChange)}",
            secondaryText = null
        )
    }

    // --- Helpers ---

    /**
     * Computes the difference between two nullable values.
     *
     * @return the arithmetic difference, or `0f` if either value is `null`.
     */
    private fun diff(current: Float?, previous: Float?): Float {
        if (current == null || previous == null) return 0f
        return current - previous
    }

    /**
     * Formats a body fat percentage change for display.
     *
     * @param delta the change in body fat percentage.
     * @param hasData whether both current and previous values were available.
     * @return a human-readable string, e.g. `"Fat -1.2%"` or `"No fat data"`.
     */
    private fun formatFatChange(delta: Float, hasData: Boolean): String {
        if (!hasData) return "No fat data"
        if (abs(delta) < 0.1f) return "Fat unchanged"
        return "Fat ${formatDelta(delta)}%"
    }

    /**
     * Formats a muscle mass change for display.
     *
     * @param delta the change in muscle mass (kg).
     * @param hasData whether both current and previous values were available.
     * @return a human-readable string, e.g. `"Muscle +0.8 kg"` or `"No muscle data"`.
     */
    private fun formatMuscleChange(delta: Float, hasData: Boolean): String {
        if (!hasData) return "No muscle data"
        if (abs(delta) < 0.1f) return "Muscle unchanged"
        return "Muscle ${formatDelta(delta)} kg"
    }

    /**
     * Formats a numeric delta with explicit sign.
     *
     * @param delta the numeric value to format.
     * @return a string with sign prefix, e.g. `"+1.5"` or `"-0.8"`.
     */
    private fun formatDelta(delta: Float): String {
        return if (delta >= 0f) "+${"%.1f".format(delta)}"
        else "%.1f".format(delta)
    }
}
