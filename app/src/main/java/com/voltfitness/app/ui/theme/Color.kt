package com.voltfitness.app.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Defines the custom color palette for the Volt Fitness application, structured according to the
 * principles of a design system. This file centralizes all color definitions to ensure
 * consistency across the UI.
 *
 * The naming convention follows a `Domain-Role-Variant` pattern (e.g., `Volt-Text-Primary`).
 */

// region ========== PRIMARY COLORS ==========
/**
 * Core brand colors used for primary actions, buttons, and prominent UI elements.
 */
val VoltCyan = Color(0xFF2F588E)          // Main brand color for primary buttons and calls-to-action.
val VoltCyanDark = Color(0xFF1E3A5F)      // A darker shade of the primary color, often used for gradient starts or pressed states.
val VoltCyanLight = Color(0xFF4A7CB8)     // A lighter variant for hover, focus, or active states.
// endregion

// region ========== BACKGROUND & SURFACE COLORS ==========
/**
 * Colors for backgrounds, surfaces, and container elements that hold content.
 */
val VoltBackgroundDark = Color(0xFF1A1A1A)    // The main, near-black background color for screens.
val VoltSurfaceDark = Color(0xFF242424)       // Default surface color for components like cards and bottom sheets.
val VoltSurfaceVariant = Color(0xFF353535)    // A slightly lighter surface for elevated or layered components.
// endregion

// region ========== TEXT COLORS ==========
/**
 * A tiered system for text colors to establish a clear visual hierarchy.
 */
val VoltTextPrimary = Color(0xFFF5F5F5)       // For headlines and primary body text (soft white).
val VoltTextSecondary = Color(0xFFB0B0B0)     // For subheadings, metadata, and less important text (light gray).
val VoltTextTertiary = Color(0xFF808080)      // For hints, captions, and tertiary information (medium gray).
val VoltTextDisabled = Color(0xFF4D4D4D)      // For disabled text and inert UI elements.
// endregion

// region ========== SEMANTIC COLORS ==========
/**
 * Contextual colors that convey meaning about the state of the application (e.g., success, error).
 */
val VoltSuccess = Color(0xFF39FF14)           // Bright lime green to indicate success, achievements, or personal records (PRs).
val VoltSuccessDim = Color(0xFF2DB80E)        // A dimmer version of success, suitable for backgrounds or less prominent success states.
val VoltError = Color(0xFFFF4444)             // A soft red used for error messages, validation failures, and critical alerts.
val VoltErrorDim = Color(0xFFCC3636)          // A dimmer version of error, suitable for error-state backgrounds.
val VoltWarning = Color(0xFFFFA726)           // An orange hue for warning messages or potentially risky actions.
val VoltInfo = Color(0xFF42A5F5)              // A neutral blue for informational tips, guides, and general notices.
// endregion

// region ========== ACCENT COLORS ==========
/**
 * Secondary colors used to add visual interest and highlight specific, non-critical UI elements.
 */
val VoltAccentBlue = Color(0xFF64B5F6)        // A secondary blue accent.
val VoltAccentPurple = Color(0xFFAB47BC)      // A secondary purple accent.
val VoltAccentOrange = Color(0xFFFF7043)      // A secondary orange accent.
// endregion

// region ========== CHART & GRAPH COLORS ==========
/**
 * A dedicated palette for data visualizations like charts and graphs to ensure consistency.
 */
val VoltChartPrimary = Color(0xFF2F588E)      // Primary color for chart data (matches brand cyan).
val VoltChartSecondary = Color(0xFF39FF14)    // Represents positive progress or secondary metrics (matches success green).
val VoltChartTertiary = Color(0xFFFF7043)     // Represents volume, intensity, or a third metric (matches accent orange).
// endregion

// region ========== BORDERS & DIVIDERS ==========
/**
 * Subtle colors for separating and defining content areas.
 */
val VoltBorderColor = Color(0xFF2A2A40)       // Subtle border color for containers and inputs.
val VoltDivider = Color(0xFF1F1F33)           // Color for horizontal or vertical divider lines.
// endregion

// region ========== OVERLAYS ==========
/**
 * Colors used for layers that sit on top of other content, such as modal dialog scrims.
 */
val VoltScrim = Color(0x99000000)             // Dark scrim (60% black) for obscuring background content behind modals or drawers.
val VoltOverlay = Color(0x33FFFFFF)           // A subtle white overlay (20% opacity) for hover effects on images or cards.
// endregion

// region ========== GRADIENT COLORS ==========
/**
 * Pre-defined start and end points for creating consistent background gradients.
 */
val VoltGradientStart = Color(0xFF1E3A5F)     // The starting color for standard background gradients (matches VoltCyanDark).
val VoltGradientEnd = Color(0xFF0F0F1E)       // The ending color for standard background gradients, fading to a deep blue-black.
// endregion
