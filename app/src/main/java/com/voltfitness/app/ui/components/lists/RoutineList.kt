package com.voltfitness.app.ui.components.lists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.R
import com.voltfitness.app.ui.components.cards.AddRoutineCard
import com.voltfitness.app.ui.components.cards.RoutineCard
import com.voltfitness.app.ui.components.cards.RoutineSummary
import com.voltfitness.app.ui.screens.home.RoutineFolder
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme
import kotlin.math.max

/**
 * High-level section that organizes workout routines into named folders.
 *
 * @param folders Hierarchical data structure containing routines grouped by category.
 * @param onRoutineClick Triggered when a routine card is selected.
 * @param onOptionsClick Triggered when the routine's context menu is requested.
 * @param onAddRoutineClick Triggered by the placeholder card to create a routine in a specific folder.
 * @param modifier Outer layout constraints.
 */
@Composable
fun RoutineSection(
    folders: List<RoutineFolder>,
    onRoutineClick: (RoutineSummary) -> Unit,
    onOptionsClick: (RoutineSummary) -> Unit,
    onAddRoutineClick: (folderId: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(VoltSpacing.small)
    ) {
        Text(
            text = "Routines",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = VoltSpacing.medium)
        )

        folders.forEach { folder ->
            RoutineHorizontalList(
                title = folder.name,
                routines = folder.routines,
                folderId = folder.id,
                onRoutineClick = onRoutineClick,
                onOptionsClick = onOptionsClick,
                onAddRoutineClick = onAddRoutineClick
            )
        }
    }
}

/**
 * A horizontal scrollable container representing a routine folder.
 * Includes a dashed placeholder at the end of the list for creation actions.
 */
@Composable
private fun RoutineHorizontalList(
    title: String,
    routines: List<RoutineSummary>,
    folderId: Long,
    onRoutineClick: (RoutineSummary) -> Unit,
    onOptionsClick: (RoutineSummary) -> Unit,
    onAddRoutineClick: (folderId: Long) -> Unit,
) {
    val density = LocalDensity.current
    var maxCardHeightPx by remember { mutableIntStateOf(0) }

    val cardHeight = with(density) {
        if (maxCardHeightPx > 0) maxCardHeightPx.toDp() else 0.dp
    }

    val baseModifier = Modifier.width(300.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = VoltSpacing.small)
    ) {
        FolderHeader(title = title)

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = VoltSpacing.medium,
                vertical = VoltSpacing.small
            ),
            horizontalArrangement = Arrangement.spacedBy(VoltSpacing.medium)
        ) {
            items(items = routines, key = { it.id }) { routine ->
                RoutineCard(
                    routine = routine,
                    onCardClick = { onRoutineClick(routine) },
                    onOptionsClick = { onOptionsClick(routine) },
                    modifier = baseModifier
                        .then(if (cardHeight > 0.dp) Modifier.height(cardHeight) else Modifier)
                        .onSizeChanged { size ->
                            maxCardHeightPx = max(maxCardHeightPx, size.height)
                        }
                )
            }

            item(key = "add_routine_$folderId") {
                AddRoutineCard(
                    onClick = { onAddRoutineClick(folderId) },
                    modifier = baseModifier
                        .then(if (cardHeight > 0.dp) Modifier.height(cardHeight) else Modifier)
                        .onSizeChanged { size ->
                            maxCardHeightPx = max(maxCardHeightPx, size.height)
                        }
                )
            }
        }
    }
}

/**
 * Internal header for folder categories.
 */
@Composable
private fun FolderHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = VoltSpacing.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_gym_folder),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(VoltSpacing.small))
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// region Previews
@Preview(showBackground = true)
@Composable
private fun RoutineSectionPreview() {
    val mockFolders = listOf(
        RoutineFolder(
            id = 1,
            name = "My Daily Routines",
            routines = listOf(
                RoutineSummary(1, "Basic Routine", 8, "Chest, Shoulders, Triceps", true),
            )
        )
    )

    VoltTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            RoutineSection(
                folders = mockFolders,
                onRoutineClick = {},
                onOptionsClick = {},
                onAddRoutineClick = {},
                modifier = Modifier.padding(vertical = VoltSpacing.medium)
            )
        }
    }
}