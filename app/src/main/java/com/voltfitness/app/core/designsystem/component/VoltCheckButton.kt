package com.voltfitness.app.core.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * A selectable button component that integrates a [Checkbox] with a text label.
 *
 * Provides a larger touch target for selection actions. The background color
 * automatically switches to [MaterialTheme.colorScheme.primaryContainer] when selected.
 *
 * @param label The text description to display.
 * @param checked Whether the button is currently in the selected state.
 * @param onCheckedChange Callback triggered when the button is clicked, providing the toggled value.
 * @param modifier Layout adjustments for the button container.
 * @param enabled Controls the enabled state of both the button and the internal checkbox.
 */
@Composable
fun VoltCheckButton(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = { onCheckedChange(!checked) },
        modifier = modifier.height(48.dp),
        enabled = enabled,
        shape = voltFieldShape,
        colors = if (checked) {
            ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else {
            ButtonDefaults.outlinedButtonColors()
        }
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            modifier = Modifier.size(18.dp),
            enabled = enabled
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}