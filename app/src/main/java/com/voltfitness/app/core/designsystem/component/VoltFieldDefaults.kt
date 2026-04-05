package com.voltfitness.app.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

/**
 * Provides a unified color palette for [OutlinedTextField] components.
 *
 * Centralizes the visual representation of states (focused, error, disabled)
 * to maintain brand consistency across the application.
 *
 * @return [TextFieldColors] configured with Volt design system tokens.
 */
@Composable
fun voltFieldColors(isError: Boolean = false): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    errorBorderColor = MaterialTheme.colorScheme.error,

    disabledBorderColor =
        if (isError) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.outline,
    disabledTextColor = MaterialTheme.colorScheme.onSurface,
    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    disabledLeadingIconColor =
        if (isError) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.onSurfaceVariant,
    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,

    errorLabelColor = MaterialTheme.colorScheme.error,
    errorSupportingTextColor = MaterialTheme.colorScheme.error,
    errorPrefixColor = MaterialTheme.colorScheme.error,
    errorSuffixColor = MaterialTheme.colorScheme.error
)

/**
 * Standard modifier for input components to ensure consistent layout behavior.
 * applies [fillMaxWidth] as the default constraint for all form fields.
 */
fun Modifier.voltFieldModifier(modifier: Modifier = Modifier): Modifier =
    this
        .fillMaxWidth()
        .then(modifier)

val voltFieldShape: Shape
    @Composable
    get() = MaterialTheme.shapes.medium

val voltSectionShape: Shape
    @Composable
    get() = MaterialTheme.shapes.large