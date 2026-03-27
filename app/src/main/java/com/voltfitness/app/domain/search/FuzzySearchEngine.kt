package com.voltfitness.app.domain.search

import com.voltfitness.app.domain.model.Exercise

/**
 * In-memory fuzzy search engine for exercises using weighted Levenshtein distance.
 *
 * **Algorithm**: For each exercise, computes a weighted similarity score across
 * multiple fields (name, target muscle, body part, equipment). The name field
 * has the highest weight since users most commonly search by exercise name.
 *
 * **Why not SQL LIKE?** LIKE '%pulldonw%' returns nothing for typos.
 * Fuzzy search with Levenshtein distance tolerates up to N character edits,
 * so "pulldonw" matches "pulldown" (distance = 1).
 *
 * **Performance**: With ~1300 exercises, in-memory comparison takes <50ms
 * on modern devices. No need for a server-side solution.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Levenshtein_distance">Levenshtein distance</a>
 */
object FuzzySearchEngine {

    /**
     * Weight multipliers for each searchable field.
     * Name is most important (3x), followed by target muscle (2x),
     * then body part and equipment (1x each).
     */
    private const val WEIGHT_NAME = 3.0
    private const val WEIGHT_TARGET = 2.0
    private const val WEIGHT_BODY_PART = 1.0
    private const val WEIGHT_EQUIPMENT = 1.0

    /** Minimum score threshold (0.0 to 1.0) to include in results. */
    private const val MIN_SCORE_THRESHOLD = 0.3

    /**
     * Searches exercises using fuzzy matching with weighted scoring.
     *
     * @param query The user's search input (e.g., "pulldonw").
     * @param exercises The full list of exercises to search against.
     * @param maxResults Maximum number of results to return.
     * @return Exercises sorted by relevance (highest score first).
     */
    fun search(
        query: String,
        exercises: List<Exercise>,
        maxResults: Int = 50
    ): List<Exercise> {
        if (query.isBlank()) return exercises.take(maxResults)

        val normalizedQuery = query.trim().lowercase()

        return exercises
            .map { exercise -> exercise to calculateScore(normalizedQuery, exercise) }
            .filter { (_, score) -> score >= MIN_SCORE_THRESHOLD }
            .sortedByDescending { (_, score) -> score }
            .take(maxResults)
            .map { (exercise, _) -> exercise }
    }

    /**
     * Calculates a weighted relevance score for an exercise against the query.
     *
     * The score combines:
     * 1. Contains-match bonus (if the field contains the exact query substring)
     * 2. Fuzzy similarity via normalized Levenshtein distance
     * 3. Token-level matching (splits multi-word queries and checks each token)
     */
    private fun calculateScore(query: String, exercise: Exercise): Double {
        val fields = listOf(
            exercise.name.lowercase() to WEIGHT_NAME,
            exercise.target.lowercase() to WEIGHT_TARGET,
            exercise.bodyPart.lowercase() to WEIGHT_BODY_PART,
            exercise.equipment.lowercase() to WEIGHT_EQUIPMENT
        )

        var totalScore = 0.0
        var totalWeight = 0.0

        for ((fieldValue, weight) in fields) {
            totalWeight += weight

            // Exact substring match gets a high bonus
            if (fieldValue.contains(query)) {
                totalScore += weight * 1.0
                continue
            }

            // Token-level: check if any word in the field starts with the query
            val fieldTokens = fieldValue.split(" ", "-", "_")
            val queryTokens = query.split(" ", "-", "_")

            val tokenScore = queryTokens.maxOf { qToken ->
                fieldTokens.maxOf { fToken ->
                    // Prefix match bonus
                    if (fToken.startsWith(qToken)) 0.85
                    // Fuzzy similarity on individual tokens
                    else normalizedSimilarity(qToken, fToken)
                }
            }

            totalScore += weight * tokenScore
        }

        return totalScore / totalWeight
    }

    /**
     * Computes normalized similarity between two strings.
     * Returns a value between 0.0 (completely different) and 1.0 (identical).
     *
     * Formula: 1.0 - (levenshteinDistance / maxLength)
     */
    private fun normalizedSimilarity(a: String, b: String): Double {
        if (a == b) return 1.0
        val maxLen = maxOf(a.length, b.length)
        if (maxLen == 0) return 1.0
        return 1.0 - (levenshteinDistance(a, b).toDouble() / maxLen)
    }

    /**
     * Standard Levenshtein Distance using dynamic programming.
     *
     * Computes the minimum number of single-character edits
     * (insertions, deletions, substitutions) to transform [a] into [b].
     *
     * Time complexity: O(n*m) where n and m are string lengths.
     * Space complexity: O(min(n,m)) using two-row optimization.
     */
    private fun levenshteinDistance(a: String, b: String): Int {
        if (a.isEmpty()) return b.length
        if (b.isEmpty()) return a.length

        // Space optimization: only keep two rows
        var previousRow = IntArray(b.length + 1) { it }
        var currentRow = IntArray(b.length + 1)

        for (i in 1..a.length) {
            currentRow[0] = i
            for (j in 1..b.length) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                currentRow[j] = minOf(
                    currentRow[j - 1] + 1,      // Insertion
                    previousRow[j] + 1,          // Deletion
                    previousRow[j - 1] + cost    // Substitution
                )
            }
            val temp = previousRow
            previousRow = currentRow
            currentRow = temp
        }

        return previousRow[b.length]
    }
}
