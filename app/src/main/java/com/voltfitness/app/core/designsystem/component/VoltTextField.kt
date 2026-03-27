package com.voltfitness.app.core.designsystem.component

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun VoltTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    suffix: String? = null,
    modifier: Modifier = Modifier,
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
        modifier = modifier.voltFieldModifier(),
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary
                )
            }
        },
        suffix = suffix?.let { { Text(it) } },
        supportingText = {
            if (isError && errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.basicMarquee(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        isError = isError,
        enabled = enabled,
        readOnly = readOnly,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = computedKeyboardType,
            imeAction = imeAction
        ),
        shape = MaterialTheme.shapes.medium,
        colors = voltFieldColors()
    )
}

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
                    label = "User name",
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
                    errorMessage = "The email format is invalid."
                )

                VoltTextField(
                    value = "Can't modify this",
                    onValueChange = {},
                    label = "Blocked Field",
                    enabled = false
                )
            }
        }
    }
}

@Preview(name = "Error", showBackground = true)
@Composable
private fun VoltTextFieldErrorPreview() {
    VoltTheme {
        VoltTextField(
            modifier = Modifier.padding(16.dp),
            value = "Incorrect value",
            onValueChange = {},
            label = "Input error",
            isError = true,
            errorMessage = "This field is required."
        )
    }
}