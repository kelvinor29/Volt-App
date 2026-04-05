package com.voltfitness.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes

/**
 * Volt Fitness Design System: Geometric Shapes.
 *
 * Maps [VoltRadius] tokens to Material 3 semantic categories.
 * This ensures standard components like Cards and Buttons automatically
 * follow the brand's roundness vocabulary.
 */
val VoltShapes = Shapes(
    // Used for small elements like Chips or Tooltips
    small = RoundedCornerShape(VoltRadius.small),

    // Used for input fields, buttons, and selection components (voltFieldShape)
    medium = RoundedCornerShape(VoltRadius.medium),

    // Used for primary containers like Routine cards (voltSectionShape)
    large = RoundedCornerShape(VoltRadius.large),

    // Used for Bottom Sheets and large Modals
    extraLarge = RoundedCornerShape(VoltRadius.extraLarge)
)