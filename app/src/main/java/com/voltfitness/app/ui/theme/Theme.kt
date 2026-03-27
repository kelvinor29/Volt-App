package com.voltfitness.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * VOLT THEME
 */

// ========== COLOR SCHEME ==========
private val VoltDarkColorScheme = darkColorScheme(
    // Primary colors (Cyan)
    primary = VoltCyan,
    onPrimary = Color.White,
    primaryContainer = VoltCyanDark,
    onPrimaryContainer = VoltCyanLight,

    // Secondary colors (Superficie elevada)
    secondary = VoltSurfaceVariant,
    onSecondary = VoltTextPrimary,
    secondaryContainer = VoltSurfaceDark,
    onSecondaryContainer = VoltTextSecondary,

    // Tertiary colors (Accent)
    tertiary = VoltAccentBlue,
    onTertiary = Color.White,
    tertiaryContainer = VoltAccentPurple,
    onTertiaryContainer = Color.White,

    // Background
    background = VoltBackgroundDark,
    onBackground = VoltTextPrimary,

    // Surface (Cards, panels)
    surface = VoltSurfaceDark,
    onSurface = VoltTextPrimary,
    surfaceVariant = VoltSurfaceVariant,
    onSurfaceVariant = VoltTextSecondary,

    // Surface Tint
    surfaceTint = VoltCyan,

    // Outline
    outline = VoltBorderColor,
    outlineVariant = VoltDivider,

    // Inverse colors (para snackbars, etc.)
    inverseSurface = VoltTextPrimary,
    inverseOnSurface = VoltBackgroundDark,
    inversePrimary = VoltCyanDark,

    // Scrim (overlay de modales)
    scrim = VoltScrim,

    // Error
    error = VoltError,
    onError = Color.White,
    errorContainer = VoltErrorDim,
    onErrorContainer = Color.White
)

// ========== MAIN THEME COMPOSABLE ==========
@Composable
fun VoltTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    // Forzar dark theme siempre (ignorar system theme)
    val colorScheme = VoltDarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VoltTypography,
        content = content
    )
}
