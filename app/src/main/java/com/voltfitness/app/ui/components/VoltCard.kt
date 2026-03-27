package com.voltfitness.app.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.voltfitness.app.ui.theme.VoltBackgroundDark
import com.voltfitness.app.ui.theme.VoltElevation
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltSurfaceDark
import com.voltfitness.app.ui.theme.VoltSurfaceVariant
import com.voltfitness.app.ui.theme.VoltTextPrimary

/**
 *   Volt Card - ATOM
 *   Reusable base card
 *
 *   This card only handles DESIGN (Elevation, color, padding, etc.)
 *   CONTENT is passed through a slot (lambda)
 */

enum class VoltBorderStyle { SOLID, DASHED }

@Composable
fun VoltCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null, // <- Optional click action
    backgroundColor: Color = VoltSurfaceDark,
    enabled: Boolean = true,
    borderStyle: VoltBorderStyle? = VoltBorderStyle.SOLID,
    content: @Composable ColumnScope.() -> Unit // <- SLOT for content
) {
    val cardColors = CardDefaults.cardColors(
        containerColor = backgroundColor,
        disabledContainerColor = backgroundColor.copy(alpha = 0.6f)
    )

    // If backgroundColor is transparent, don't apply elevation
    val finalElevation = if (backgroundColor == Color.Transparent) 0.dp else VoltElevation.small
    val cardElevation = CardDefaults.cardElevation(defaultElevation = finalElevation)

    val shape = MaterialTheme.shapes.large
    val borderWidth: Dp = 2.dp
    val borderColor: Color = VoltSurfaceVariant

    // Border configuration
    val borderModifier = if (borderStyle != null) {
        val finalBorderColor = borderColor.copy(alpha = if (enabled) 1f else 0.6f)
        when (borderStyle) {
            VoltBorderStyle.SOLID -> Modifier.border(borderWidth, finalBorderColor, shape)
            VoltBorderStyle.DASHED -> {
                Modifier.drawWithContent {
                    drawContent()
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    drawOutline(
                        outline = shape.createOutline(size, layoutDirection, this),
                        color = finalBorderColor,
                        style = Stroke(
                            width = borderWidth.toPx(),
                            pathEffect = pathEffect
                        )
                    )
                }
            }
        }
    } else Modifier

    val finalModifier = modifier
        .fillMaxWidth()
        .then(borderModifier)

    // Clickable only if a click action is provided (onClick != null)
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = finalModifier,
            colors = cardColors,
            elevation = cardElevation,
            shape = shape,
            enabled = enabled,
            content = content
        )
    } else {
        Card(
            modifier = finalModifier,
            colors = cardColors,
            elevation = cardElevation,
            shape = shape,
            content = content
        )
    }
}

// region Previews
@Preview(name = "Static Card")
@Composable
fun VoltCardPreview() {
    VoltCard {
        Text(
            text = "This is a Static Card",
            modifier = Modifier.padding(VoltSpacing.medium),
            color = VoltTextPrimary
        )
    }
}

@Preview(name = "Clickable Card")
@Composable
fun VoltCardClickablePreview() {
    VoltCard(
        onClick = { },
    ) {
        Text(
            text = "This is a Clickable Card",
            modifier = Modifier.padding(VoltSpacing.medium),
            color = VoltTextPrimary
        )
    }
}

@Preview(showBackground = true, name = "Transparent Card")
@Composable
fun VoltCardTransparentPreview() {
    VoltCard(
        backgroundColor = Color.Transparent,
    ) {
        Text(
            text = "I'm transparent and reusable!",
            modifier = Modifier.padding(VoltSpacing.medium),
            color = VoltBackgroundDark
        )
    }
}

@Preview(
    showBackground = true,
    name = "Transparent Card with Dashed Border",
    backgroundColor = 252525
)
@Composable
fun VoltCardTransparentDashedBorderPreview() {
    VoltCard(
        borderStyle = VoltBorderStyle.DASHED,
        backgroundColor = Color.Transparent,
    ) {
        Text(
            "This card has a dashed border", modifier = Modifier.padding(VoltSpacing.medium),
            color = VoltTextPrimary
        )
    }
}

@Preview(showBackground = true, name = "Disabled Card")
@Composable
fun VoltCardDisabledPreview() {
    VoltCard(
        onClick = { },
        enabled = false // Disabled
    ) {
        Text(
            text = "This is a Disabled Card",
            modifier = Modifier.padding(VoltSpacing.medium),
            color = VoltTextPrimary
        )
    }
}

// endregion
