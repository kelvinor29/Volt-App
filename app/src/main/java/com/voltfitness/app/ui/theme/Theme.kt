package com.voltfitness.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Volt Fitness Dark Color Scheme configuration.
 *
 * Maps brand-specific tokens to standard Material 3 semantic roles.
 * Designed for high-contrast OLED displays with a focus on deep backgrounds.
 */
private val VoltDarkColorScheme = darkColorScheme(
    primary = VoltCyan,
    onPrimary = Color.White,
    primaryContainer = VoltCyanDark,
    onPrimaryContainer = VoltTextPrimary,

    secondary = VoltSurfaceVariant,
    onSecondary = VoltTextPrimary,
    secondaryContainer = VoltSurfaceDark,
    onSecondaryContainer = VoltTextSecondary,

    tertiary = VoltAccentBlue,
    onTertiary = Color.White,
    tertiaryContainer = VoltAccentPurple,
    onTertiaryContainer = Color.White,

    background = VoltBackgroundDark,
    onBackground = VoltTextPrimary,

    surface = VoltSurfaceDark,
    onSurface = VoltTextPrimary,
    surfaceVariant = VoltSurfaceVariant,
    onSurfaceVariant = VoltTextSecondary,

    surfaceTint = VoltCyan,

    outline = VoltBorderColor,
    outlineVariant = VoltDivider,

    inverseSurface = VoltTextPrimary,
    inverseOnSurface = VoltBackgroundDark,
    inversePrimary = VoltCyanDark,

    scrim = VoltScrim,

    error = VoltError,
    onError = Color.White,
    errorContainer = VoltErrorDim,
    onErrorContainer = Color.White
)

private val VoltLightColorScheme = lightColorScheme(
    primary = VoltCyan,
    onPrimary = Color.White,
    primaryContainer = VoltCyanLight,
    onPrimaryContainer = VoltCyanDark,

    secondary = VoltSurfaceVariantLight,
    onSecondary = VoltTextPrimaryLight,
    secondaryContainer = VoltSurfaceLight,
    onSecondaryContainer = VoltTextSecondaryLight,

    tertiary = VoltAccentBlue,
    onTertiary = Color.White,

    background = VoltBackgroundLight,
    onBackground = VoltTextPrimaryLight,

    surface = VoltSurfaceLight,
    onSurface = VoltTextPrimaryLight,
    surfaceVariant = VoltSurfaceVariantLight,
    onSurfaceVariant = VoltTextSecondaryLight,

    outline = VoltBorderLight,
    outlineVariant = VoltDividerLight,

    error = VoltError,
    onError = Color.White
)

/**
 * Root theme composable for the Volt Fitness application.
 *
 * Configures [MaterialTheme] with brand colors, typography, and shapes.
 * Forces Dark Theme regardless of system settings to maintain brand identity.
 *
 * @param content The UI hierarchy to be themed.
 */
@Composable
fun VoltTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) VoltDarkColorScheme else VoltLightColorScheme
    val view = LocalView.current

    // Update status bar appearance to match brand background
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VoltTypography,
        shapes = VoltShapes,
        content = content
    )
}