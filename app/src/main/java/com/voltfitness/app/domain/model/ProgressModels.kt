package com.voltfitness.app.domain.model

import com.voltfitness.app.ui.common.UiText

enum class ProgressStatus {
    POSITIVE,
    NEUTRAL,
    NEGATIVE
}

data class ProgressEvaluation(
    val status: ProgressStatus,
    val primaryText: UiText,
    val secondaryText: UiText?
)