package com.voltfitness.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Volt Fitness Design System: Color Palette.
 *
 * Centralizes all color definitions to ensure brand consistency.
 * Naming Convention: Domain-Role-Variant (e.g., VoltCyanDark).
 */

// region ========== PRIMARY COLORS ==========

val VoltCyan = Color(0xFF2F588E)      // Brand primary
val VoltCyanDark = Color(0xFF1E3A5F)  // Primary variant for gradients/pressed states
val VoltCyanLight = Color(0xFF4A7CB8) // Primary variant for hover/focus

// endregion

// region ========== BACKGROUND & SURFACE COLORS ==========

val VoltBackgroundDark = Color(0xFF1A1A1A) // Root screen background
val VoltSurfaceDark = Color(0xFF242424)    // Component surface (Cards, Sheets)
val VoltSurfaceVariant = Color(0xFF353535) // Layered/Elevated surface

// endregion

// region ========== TEXT COLORS ==========

val VoltTextPrimary = Color(0xFFF5F5F5)   // Headlines and body text
val VoltTextSecondary = Color(0xFFB0B0B0) // Metadata and subheadings
val VoltTextTertiary = Color(0xFF808080)  // Hints and captions
val VoltTextDisabled = Color(0xFF4D4D4D)  // Inert elements

// endregion

// region ========== SEMANTIC COLORS ==========

val VoltSuccess = Color(0xFF39FF14)    // Achievements and success states
val VoltSuccessDim = Color(0xFF2DB80E) // Success background variant
val VoltError = Color(0xFFFF4444)      // Validation failures and alerts
val VoltErrorDim = Color(0xFFCC3636)   // Error background variant
val VoltWarning = Color(0xFFFFA726)    // Risk warnings
val VoltInfo = Color(0xFF42A5F5)       // Informational guides

// endregion

// region ========== ACCENT COLORS ==========

val VoltAccentBlue = Color(0xFF64B5F6)
val VoltAccentPurple = Color(0xFFAB47BC)
val VoltAccentOrange = Color(0xFFFF7043)

// endregion

// region ========== CHART COLORS ==========

val VoltChartPrimary = Color(0xFF2F588E)   // Brand cyan
val VoltChartSecondary = Color(0xFF39FF14) // Success green
val VoltChartTertiary = Color(0xFFFF7043)  // Accent orange

// endregion

// region ========== BORDERS & DIVIDERS ==========

val VoltBorderColor = Color(0xFF2A2A40) // Container outlines
val VoltDivider = Color(0xFF1F1F33)     // Layout separators

// endregion

// region ========== OVERLAYS ==========

val VoltScrim = Color(0x99000000)   // 60% Black for modals
val VoltOverlay = Color(0x33FFFFFF) // 20% White for interactions

// endregion

// region ========== GRADIENT COLORS ==========

val VoltGradientStart = Color(0xFF1E3A5F) // Cyan Dark
val VoltGradientEnd = Color(0xFF0F0F1E)   // Deep blue-black

// endregion

// region ========== LIGHT MODE SPECIFIC COLORS ==========
val VoltBackgroundLight = Color(0xFFF8F9FA)
val VoltSurfaceLight = Color(0xFFFFFFFF)
val VoltSurfaceVariantLight = Color(0xFFE9ECEF)

val VoltTextPrimaryLight = Color(0xFF1A1A1A)
val VoltTextSecondaryLight = Color(0xFF495057)
val VoltTextTertiaryLight = Color(0xFF6C757D)

val VoltBorderLight = Color(0xFFDEE2E6)
val VoltDividerLight = Color(0xFFCED4DA)
// endregion