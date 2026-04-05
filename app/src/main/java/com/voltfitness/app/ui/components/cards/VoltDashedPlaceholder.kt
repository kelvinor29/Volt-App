package com.voltfitness.app.ui.components.cards

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.designsystem.component.voltSectionShape
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * A decorative placeholder component with a dashed border used for "Add" actions or empty states.
 *
 * Reuses [voltSectionShape] to ensure the dashed stroke aligns with the application's
 * container style.
 *
 * @param text The descriptive label displayed below the icon.
 * @param icon The visual representation of the action (e.g., Icons.Default.Add).
 * @param onClick Execution trigger when the placeholder is tapped.
 * @param modifier Layout modifiers for the container.
 * @param containerHeight Fixed height for the placeholder box. Defaults to 80.dp.
 * @param iconSize Dimensions for the central icon. Defaults to 32.dp.
 */
@Composable
fun VoltDashedPlaceholder(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerHeight: Dp = 80.dp,
    iconSize: Dp = 32.dp
) {
    val borderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
    val contentAlpha = 0.6f

    val shape = voltSectionShape

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(containerHeight)
            .clip(shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val cornerRadiusPx = 16.dp.toPx()
            val dashEffect = PathEffect.dashPathEffect(
                floatArrayOf(12.dp.toPx(), 8.dp.toPx()),
                0f
            )

            drawRoundRect(
                color = borderColor,
                style = Stroke(width = 2.dp.toPx(), pathEffect = dashEffect),
                cornerRadius = CornerRadius(cornerRadiusPx)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = contentAlpha),
                modifier = Modifier.size(iconSize)
            )

            Spacer(Modifier.height(VoltSpacing.small))

            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = contentAlpha),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewVoltDashedPlaceholder() {
    VoltTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            VoltDashedPlaceholder(
                text = "Agregar nuevo ejercicio",
                icon = Icons.Default.Add,
                onClick = { /* Acción de prueba */ }
            )
        }
    }
}

@Preview(showBackground = true, name = "Dark Mode", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewVoltDashedPlaceholderDark() {
    VoltTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            VoltDashedPlaceholder(
                text = "Añadir rutina",
                icon = Icons.Default.Add,
                onClick = { /* Acción de prueba */ }
            )
        }
    }
}