package com.voltfitness.app.domain.model

enum class ProgressStatus {
    POSITIVE,
    NEUTRAL,
    NEGATIVE
}

data class ProgressEvaluation(
    val status: ProgressStatus,
    val primaryText: String,
    val secondaryText: String?
)