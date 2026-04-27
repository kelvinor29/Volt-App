package com.voltfitness.app.ui.screens.home.components.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.ui.components.cards.RoutineDayCard
import com.voltfitness.app.ui.screens.home.ActiveRoutineDayUi
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme
import timber.log.Timber
import kotlin.math.max

/**
 * Horizontal scrollable section that lists every training day of the active routine.
 *
 * Mirrors the structural pattern of [RoutineSection]: a bold section title followed by a
 * [LazyRow] of uniform-width cards. Renders nothing when [days] is empty so the Home
 * screen stays clean for users who have not yet set an active routine.
 *
 * @param days           Ordered list of training days belonging to the active routine.
 * @param onStartWorkout Propagates the selected day's ID up to the screen / NavController.
 * @param modifier       Outer layout constraints.
 */
@Composable
fun ActiveRoutineDayList(
    days: List<ActiveRoutineDayUi>,
    onStartWorkout: (dayId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (days.isEmpty()) return

    val density = LocalDensity.current
    var maxCardHeightPx by remember { mutableIntStateOf(0) }

    val cardHeight = with(density) {
        if (maxCardHeightPx > 0) maxCardHeightPx.toDp() else 0.dp
    }
    val baseModifier = Modifier.width(300.dp)


    LazyRow(
        contentPadding = PaddingValues(
            horizontal = VoltSpacing.medium,
            vertical = VoltSpacing.small
        ),
        horizontalArrangement = Arrangement.spacedBy(VoltSpacing.medium)
    ) {
        items(items = days, key = { it.dayId }) { day ->
            RoutineDayCard(
                day = day,
                onStartWorkout = onStartWorkout,
                modifier = baseModifier
                    .then(if (cardHeight > 0.dp) Modifier.height(cardHeight) else Modifier)
                    .onSizeChanged { size ->
                        max(maxCardHeightPx, size.height)
                    })
        }
    }
}

// region Previews
@Preview(showBackground = true)
@Composable
private fun ActiveRoutineDayListPreview() {
    val mockDays = listOf(
        ActiveRoutineDayUi(1L, 1, "Push Day", "Chest, Shoulders, Triceps"),
        ActiveRoutineDayUi(2L, 2, "Pull Day", "Back, Biceps"),
        ActiveRoutineDayUi(3L, 3, "Leg Day", "Quads, Hamstrings, Glutes"),
    )

    VoltTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ActiveRoutineDayList(
                days = mockDays,
                onStartWorkout = {},
                modifier = Modifier.padding(vertical = VoltSpacing.medium)
            )
        }
    }
}
// endregion