package com.voltfitness.app.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Volt Fitness Design System: Geometric Tokens.
 *
 * Centralizes all spacing, sizing, and elevation values to maintain
 * a consistent visual rhythm and touch-target compliance.
 */

// region ========== SPACING SYSTEM (8dp base) ==========
object VoltSpacing {
    val none = 0.dp
    val extraSmall = 4.dp      // xs
    val small = 8.dp           // sm
    val medium = 16.dp         // md (baseline)
    val large = 24.dp          // lg
    val extraLarge = 32.dp     // xl
    val extraExtraLarge = 48.dp // xxl
    val huge = 64.dp           // huge
    val xhuge = 80.dp          // xhuge
}

// region ========== CORNER RADIUS ==========
object VoltRadius {
    val none = 0.dp
    val small = 4.dp           // Chips, tags, small badges
    val medium = 8.dp          // Selection buttons, small cards
    val large = 12.dp          // Primary cards (Routine, Exercise)
    val extraLarge = 16.dp     // Modals, Bottom Sheets, Dialogs
    val round = 100.dp         // Pill-shaped elements
}

// region ========== ELEVATIONS (Shadows) ==========
object VoltElevation {
    val none = 0.dp
    val small = 2.dp           // Subtle surface separation
    val medium = 4.dp          // Elevated buttons and focused cards
    val large = 8.dp           // Overlays and system dialogs
    val extraLarge = 16.dp     // Floating Action Buttons
}

// region ========== BORDER WIDTH ==========
object VoltBorder {
    val none = 0.dp
    val thin = 1.dp            // Standard outlines
    val medium = 2.dp          // Emphasized state borders
    val thick = 4.dp           // Selection indicators
}

// region ========== ICON SIZES ==========
object VoltIconSize {
    val small = 16.dp          // Inline label icons
    val medium = 24.dp         // Standard navigation icons
    val large = 32.dp          // Featured header icons
    val extraLarge = 48.dp     // Hero/Illustration icons
}

// region ========== BUTTON SIZES ==========
object VoltButtonSize {
    val small = 36.dp          // Secondary compact actions
    val medium = 48.dp         // Standard accessible touch target
    val large = 56.dp          // Primary call-to-action buttons
}