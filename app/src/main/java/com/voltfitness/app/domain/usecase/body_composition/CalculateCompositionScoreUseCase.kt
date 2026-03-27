package com.voltfitness.app.domain.usecase.body_composition

/**
 * Business logic provider for evaluating physical condition through a Body Composition Score.
 * * This Use Case mimics medical-grade analysis (similar to InBody® systems) to synthesize
 * complex biometric data into a single, intuitive metric. It evaluates weight, body fat,
 * lean mass, and metabolic risks while adjusting for physiological factors such as age and gender.
 *
 * Calculations follow a multi-step weighted algorithm:
 * 1. **Lean Body Mass (LBM) Balance (~35%):** Comparison between actual lean mass and
 *    age-adjusted ideal targets.
 * 2. **Fat Mass Evaluation (~25%):** Bonus for being near the healthy range,
 *    penalty for excessive fat tissue.
 * 3. **Muscle Quality (~10%):** Direct assessment of Skeletal Muscle Mass (SMM).
 * 4. **Visceral Risk (~10%):** Impact of internal fat on metabolic health.
 * 5. **Morphological Risk (~5%):** Waist-to-Hip Ratio (WHR) analysis.
 *
 * Designed as a Singleton [object] to facilitate cross-layer usage without requiring
 * manual DI instantiation for stateless mathematical operations.
 */
object CalculateCompositionScoreUseCase {

    /**
     * Executes the composition score calculation.
     *
     * @param weightKg Total body weight in Kilograms.
     * @param bodyFatPercent Percentage of total body fat.
     * @param muscleMassKg Total skeletal muscle mass in Kilograms.
     * @param visceralFatPercent Level/Percentage of visceral (organ) fat.
     * @param waistHipRatio The ratio of waist circumference to hip circumference.
     * @param heightCm Stature in Centimeters.
     * @param gender Biological gender ("male" or "female") used for hormonal/metabolic scaling.
     * @param age Chronological age used to adjust muscle retention expectations (Sarcopenia factor).
     * @return A consolidated score ranging from 40.0 (High Risk/Imbalance) to 120.0 (Peak Conditioning).
     */
    fun calculate(
        weightKg: Float,
        bodyFatPercent: Float?,
        muscleMassKg: Float?,
        visceralFatPercent: Float?,
        waistHipRatio: Float?,
        heightCm: Float,
        gender: String,
        age: Int
    ): Float {
        val heightM = heightCm / 100f
        val isMale = gender.equals("male", true)
        var score = 80f

        // --- Standard Reference Pre-calculations ---
        // Ideal weight based on BMI 22 (Male) or 21.5 (Female)
        val standardWeight = heightM * heightM * if (isMale) 22f else 21.5f
        val idealFatPercent = if (isMale) 15f else 23f
        val idealFatMassKg = standardWeight * (idealFatPercent / 100f)
        val idealLbmKg = standardWeight - idealFatMassKg

        // Actual values derived from weightKg + bodyFatPercent
        val actualFatMassKg = bodyFatPercent?.let { weightKg * (it / 100f) }
        val actualLbmKg = bodyFatPercent?.let { weightKg * (1f - it / 100f) }

        // Biological adjustment factor for age-related muscle loss (natural sarcopenia)
        val ageFactor = getAgeMuscleRetentionFactor(age)

        // === Step 1: Lean Body Mass Assessment (LBM) ===
        // Rewards higher muscle density relative to the ideal age-adjusted target
        actualLbmKg?.let { lbm ->
            val ageAdjustedIdealLbm = idealLbmKg * ageFactor
            if (ageAdjustedIdealLbm > 0f) {
                val delta = ((lbm - ageAdjustedIdealLbm) / ageAdjustedIdealLbm) * 15f
                score += delta.coerceIn(-15f, 15f)
            }
        }

        // === Step 2: Fat Mass Evaluation ===
        // penalizes deviation from the standardized healthy fat mass
        actualFatMassKg?.let { fm ->
            if (idealFatMassKg > 0f) {
                val delta = ((idealFatMassKg - fm) / idealFatMassKg) * 10f
                score += delta.coerceIn(-10f, 10f)
            }
        }

        // === Step 3: Skeletal Muscle Mass (SMM) Bonus ===
        // Analyzes purely contractile tissue vs. statistical expectations
        muscleMassKg?.let { mm ->
            val expectedSmm = standardWeight * if (isMale) 0.42f else 0.35f
            val ageAdjustedSmm = expectedSmm * ageFactor
            if (ageAdjustedSmm > 0f) {
                val delta = ((mm - ageAdjustedSmm) / ageAdjustedSmm) * 5f
                score += delta.coerceIn(-5f, 5f)
            }
        }

        // === Step 4: Visceral Adiposity Impact ===
        // Strong penalty for high visceral fat due to associated metabolic cardiovascular risks
        visceralFatPercent?.let { vf ->
            val delta = when {
                vf <= 9f -> 2f
                vf <= 14f -> 0f
                else -> -((vf - 14f) * 0.8f)
            }
            score += delta
        }

        // === Step 5: Waist-to-Hip Ratio (WHR) ===
        // Evaluates android vs. gynoid fat distribution
        waistHipRatio?.let { whr ->
            val idealWhr = if (isMale) 0.90f else 0.85f
            val delta = when {
                whr <= idealWhr -> 2f
                whr <= idealWhr + 0.1f -> 0f
                else -> -((whr - idealWhr) * 20f)
            }
            score += delta
        }

        return score.coerceIn(40f, 120f)
    }

    /**
     * Calculates the Muscle Retention Factor based on biological aging.
     *
     * Accounts for Natural Sarcopenia:
     * - Maintenance phase: < 30 years
     * - Progressive decline (~0.5%/year): 30-50 years
     * - Accelerated decline (~1-2%/year): > 50 years
     *
     * @param age The user's age.
     * @return Multiplier for muscle-related targets.
     */
    private fun getAgeMuscleRetentionFactor(age: Int): Float {
        return when {
            age < 30 -> 1.00f
            age < 40 -> 0.95f
            age < 50 -> 0.88f
            age < 60 -> 0.80f
            else -> 0.72f
        }
    }
}