package com.voltfitness.app.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Base modifier for all fields in the Volt layout system.
 * Ensures that all input fields have the same width,
 * without adding vertical padding (this should come from the parent container).
 */
fun Modifier.voltFieldModifier(): Modifier = this.fillMaxWidth()

/**
 * Unified colors for all text fields with borders in the design system.
 * Centralizes color behavior across states: normal, error, disabled.
 */
@Composable
fun voltFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    errorBorderColor = MaterialTheme.colorScheme.error,
    disabledBorderColor = MaterialTheme.colorScheme.outline,
    disabledTextColor = MaterialTheme.colorScheme.onSurface,
    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
)