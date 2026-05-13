package com.voltfitness.app.ui.components.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.designsystem.component.VoltButton
import com.voltfitness.app.core.designsystem.component.VoltOutlinedButton
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme
import com.voltfitness.app.R

/**
 * A persistent footer component designed for multi-step processes or confirmation screens.
 *
 * Provides a primary action and a secondary dismiss/back action in a balanced row.
 *
 * @param primaryText Label for the main action button.
 * @param onPrimaryClick Execution trigger for the primary action.
 * @param modifier Layout modifiers for the bottom bar container.
 * @param secondaryText Label for the alternative action. Defaults to "Dismiss".
 * @param onSecondaryClick Execution trigger for the secondary action.
 * @param primaryIcon Optional icon for the primary button.
 * @param primaryEnabled Enables or disables the primary action button.
 * @param primaryLoading Displays a progress indicator on the primary button when true.
 * @param shadowElevation Z-axis elevation for the bottom bar. Defaults to 6.dp.
 */
@Composable
fun VoltStepBottomBar(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    secondaryText: String? = null,
    onSecondaryClick: () -> Unit,
    primaryIcon: ImageVector? = null,
    primaryEnabled: Boolean = true,
    primaryLoading: Boolean = false,
    shadowElevation: Dp = 6.dp,
) {
    val finalSecondaryText = secondaryText ?: stringResource(id = R.string.dismiss)
    Surface(
        modifier = modifier,
        shadowElevation = shadowElevation,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = VoltSpacing.medium,
                    end = VoltSpacing.medium,
                    top = VoltSpacing.small,
                    bottom = VoltSpacing.medium
                ),
            horizontalArrangement = Arrangement.spacedBy(VoltSpacing.small),
        ) {
            // Secondary action (Usually Dismiss, Back, or Cancel)
            VoltOutlinedButton(
                text = finalSecondaryText,
                onClick = onSecondaryClick,
                modifier = Modifier.weight(1f),
            )

            // Primary action (Usually Save, Continue, or Start)
            VoltButton(
                text = primaryText,
                icon = primaryIcon,
                onClick = onPrimaryClick,
                modifier = Modifier.weight(1f),
                enabled = primaryEnabled,
                isLoading = primaryLoading,
            )
        }
    }
}

@Preview(showBackground = true, name = "Bottom Bar States")
@Composable
fun PreviewVoltStepBottomBarStates() {
    VoltTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Standard Navigation: Back/Next
            VoltStepBottomBar(
                secondaryText = "Back",
                onSecondaryClick = {},
                primaryText = "Next",
                primaryIcon = Icons.AutoMirrored.Filled.ArrowForward,
                onPrimaryClick = {}
            )

            // Confirmation: Cancel/Save (Loading state)
            VoltStepBottomBar(
                secondaryText = "Cancel",
                onSecondaryClick = {},
                primaryText = "Save Changes",
                primaryLoading = true,
                onPrimaryClick = {}
            )

            // Disabled state: Default secondary "Dismiss"
            VoltStepBottomBar(
                onSecondaryClick = {},
                primaryText = "Finish Workout",
                primaryIcon = Icons.Default.Check,
                primaryEnabled = false,
                onPrimaryClick = {}
            )
        }
    }
}

@Preview(
    showBackground = true,
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewVoltStepBottomBarDark() {
    VoltTheme {
        VoltStepBottomBar(
            secondaryText = "Cancel",
            onSecondaryClick = {},
            primaryText = "Continue",
            onPrimaryClick = {}
        )
    }
}