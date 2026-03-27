package com.voltfitness.app.ui.theme

import androidx.compose.ui.unit.dp

/**
 * VOLT DESIGN TOKENS
 * Sistema de spacing, radius, elevaciones consistente
 */

// ========== SPACING SYSTEM (8dp base) ==========
object VoltSpacing {
    val none = 0.dp
    val extraSmall = 4.dp      // xs
    val small = 8.dp           // sm
    val medium = 16.dp         // md (base)
    val large = 24.dp          // lg
    val extraLarge = 32.dp     // xl
    val extraExtraLarge = 48.dp // xxl
    val huge = 64.dp           // huge
}

// ========== CORNER RADIUS ==========
object VoltRadius {
    val none = 0.dp
    val small = 4.dp           // Chips, tags
    val medium = 8.dp          // Buttons, cards pequeños
    val large = 12.dp          // Cards principales
    val extraLarge = 16.dp     // Modales, sheets
    val round = 100.dp         // Completamente redondeado
}

// ========== ELEVATIONS (Shadows) ==========
object VoltElevation {
    val none = 0.dp
    val small = 2.dp           // Cards sutiles
    val medium = 4.dp          // Botones elevados
    val large = 8.dp           // Modales, dialogs
    val extraLarge = 16.dp     // Floating action buttons
}

// ========== BORDER WIDTH ==========
object VoltBorder {
    val none = 0.dp
    val thin = 1.dp            // Borders estándar
    val medium = 2.dp          // Borders enfatizados
    val thick = 4.dp           // Indicadores de estado
}

// ========== ICON SIZES ==========
object VoltIconSize {
    val small = 16.dp          // Icons en labels
    val medium = 24.dp         // Icons estándar
    val large = 32.dp          // Icons destacados
    val extraLarge = 48.dp     // Icons hero, ilustrativos
}

// ========== BUTTON SIZES ==========
object VoltButtonSize {
    val small = 36.dp          // Altura mínima
    val medium = 48.dp         // Altura estándar (touch target)
    val large = 56.dp          // Botones primarios grandes
}
