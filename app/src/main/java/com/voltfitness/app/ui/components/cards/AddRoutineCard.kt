package com.voltfitness.app.ui.components.cards

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.designsystem.component.voltSectionShape
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * Actionable placeholder card designed to trigger the creation of a new routine.
 *
 * It mirrors the standard [RoutineCard] dimensions and uses the design system's
 * section shape for visual consistency.
 *
 * @param onClick Callback executed when the placeholder is tapped.
 * @param modifier Layout modifiers for the card container.
 */
@Composable
fun AddRoutineCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sectionShape = voltSectionShape

    VoltDashedPlaceholder(
        text = "Add new Routine",
        icon = Icons.AutoMirrored.Outlined.NoteAdd,
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            shape = sectionShape
            clip = true
        },
        iconSize = 40.dp
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun AddRoutineCardPreview() {
    VoltTheme {
        AddRoutineCard(
            onClick = {},
            modifier = Modifier
                .width(300.dp)
                .height(130.dp)
        )
    }
}