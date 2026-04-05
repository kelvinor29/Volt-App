package com.voltfitness.app.core.designsystem.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * Standard text input for the Volt design system.
 *
 * Automatically manages styling, accessibility, and keyboard behavior.
 * When a [suffix] is present, it defaults to numeric input unless specified otherwise.
 *
 * @param value Current text state.
 * @param onValueChange Text update callback.
 * @param label Floating label text.
 * @param suffix Optional unit or metadata (e.g., "kg", "cm").
 * @param leadingIcon Visual context icon.
 * @param imeAction Keyboard action button behavior.
 * @param isError Triggers error visual state and displays [errorMessage].
 * @param isDecimal Relevant for numeric inputs with suffix; enables floating-point numbers.
 * @param errorMessage Feedback text shown in the supporting area when [isError] is true.
 * @param enabled Controls interactive and visual states.
 * @param readOnly Disables manual input but allows focus/selection.
 * @param singleLine Constraints input to one row.
 * @param keyboardType Explicit keyboard override.
 */
@Composable
fun VoltTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    suffix: String? = null,
    leadingIcon: ImageVector? = null,
    imeAction: ImeAction = ImeAction.Next,
    isError: Boolean = false,
    isDecimal: Boolean = true,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val computedKeyboardType = when {
        keyboardType != KeyboardType.Text -> keyboardType
        suffix != null -> if (isDecimal) KeyboardType.Decimal else KeyboardType.Number
        else -> KeyboardType.Text
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                modifier = Modifier.basicMarquee(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = Modifier.voltFieldModifier(modifier),
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }
        },
        suffix = suffix?.let { { Text(text = it) } },
        supportingText = if (isError && !errorMessage.isNullOrBlank()) {
            {
                Text(
                    text = errorMessage,
                    modifier = Modifier.basicMarquee(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else null,
        isError = isError,
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = computedKeyboardType,
            imeAction = imeAction
        ),
        shape = voltFieldShape,
        colors = voltFieldColors(isError = isError)
    )
}

// region Previews
@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun VoltTextFieldPreview() {
    VoltTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                VoltTextField(
                    value = "",
                    onValueChange = {},
                    label = "Username",
                    leadingIcon = Icons.Default.Person
                )

                VoltTextField(
                    value = "85.5",
                    onValueChange = {},
                    label = "Body weight",
                    leadingIcon = Icons.Default.MonitorWeight,
                    suffix = "kg"
                )

                VoltTextField(
                    value = "invalid-email",
                    onValueChange = {},
                    label = "Email",
                    leadingIcon = Icons.Default.Email,
                    isError = true,
                    errorMessage = "Please enter a valid email address."
                )
            }
        }
    }
}
// endregion