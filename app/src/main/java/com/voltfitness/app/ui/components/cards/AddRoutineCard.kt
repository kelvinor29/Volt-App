package com.voltfitness.app.ui.components.cards

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * A placeholder card that mimics [RoutineCard] dimensions but serves
 * as a call-to-action button for creating a new routine within a folder.
 *
 * Design: Dashed border, centered document-add icon, and "Add new Routine" label.
 *
 * @param onClick Callback triggered when the user taps the card.
 * @param modifier Modifier to be applied to the card container.
 */
@Composable
fun AddRoutineCard(onClick: () -> Unit, modifier: Modifier = Modifier) {
    VoltDashedPlaceholder(
        text = "Add new Routine",
        icon = Icons.AutoMirrored.Outlined.NoteAdd,
        onClick = onClick,
        modifier = modifier,
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
