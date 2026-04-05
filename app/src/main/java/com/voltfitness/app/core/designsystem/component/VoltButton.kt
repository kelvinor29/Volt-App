package com.voltfitness.app.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * Primary action button for the Volt Fitness design system.
 * Reuses [voltFieldShape] for visual consistency with input fields.
 *
 * @param text The label text.
 * @param onClick Execution trigger.
 * @param isLoading When true, displays a [CircularProgressIndicator] and disables interaction.
 * @param icon Optional leading icon.
 */
@Composable
fun VoltButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !isLoading,
        shape = voltFieldShape
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            icon?.let {
                Icon(imageVector = it, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(text)
        }
    }
}

/**
 * Medium-emphasis button with a tonal background.
 */
@Composable
fun VoltSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = voltFieldShape
    ) {
        icon?.let {
            Icon(imageVector = it, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text)
    }
}

/**
 * Low-emphasis outlined button for supplementary actions.
 */
@Composable
fun VoltOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = voltFieldShape
    ) {
        Text(text)
    }
}


@Preview(name = "Button Gallery - Light", showBackground = true)
@Preview(
    name = "Button Gallery - Dark",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun VoltButtonsPreview() {
    VoltTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                VoltButton(
                    text = "Primary Button",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )

                VoltButton(
                    text = "With Icon",
                    onClick = {},
                    icon = Icons.AutoMirrored.Filled.Login,
                    modifier = Modifier.fillMaxWidth()
                )

                VoltButton(
                    text = "Loading State",
                    onClick = {},
                    isLoading = true,
                    modifier = Modifier.fillMaxWidth()
                )

                VoltSecondaryButton(
                    text = "Secondary Tonal",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )

                VoltSecondaryButton(
                    text = "Add Exercise",
                    onClick = {},
                    icon = Icons.Default.Add,
                    modifier = Modifier.fillMaxWidth()
                )

                VoltOutlinedButton(
                    text = "Outlined Action",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                )

                VoltButton(
                    text = "Disabled Primary",
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(name = "Loading Interaction", showBackground = true)
@Composable
private fun VoltButtonLoadingPreview() {
    VoltTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            VoltButton(
                text = "Save Changes",
                onClick = {},
                icon = Icons.Default.Save,
                isLoading = true
            )
        }
    }
}