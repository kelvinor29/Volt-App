package com.voltfitness.app.ui.components.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.designsystem.component.VoltButton
import com.voltfitness.app.core.designsystem.component.VoltOutlinedButton

@Composable
fun VoltStepBottomBar(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    secondaryText: String = "Dismiss",
    onSecondaryClick: () -> Unit,
    primaryIcon: ImageVector? = null,
    primaryEnabled: Boolean = true,
    primaryLoading: Boolean = false,
    shadowElevation: Dp = 10.dp,
) {
    Surface(
        modifier = modifier,
        shadowElevation = shadowElevation,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 10.dp, top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            VoltOutlinedButton(
                text = secondaryText,
                onClick = onSecondaryClick,
                modifier = Modifier.weight(1f),
            )

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