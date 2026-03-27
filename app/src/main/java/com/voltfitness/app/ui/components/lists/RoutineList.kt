package com.voltfitness.app.ui.components.lists

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.R
import com.voltfitness.app.ui.components.cards.AddRoutineCard
import com.voltfitness.app.ui.components.cards.RoutineCard
import com.voltfitness.app.ui.components.cards.RoutineSummary
import com.voltfitness.app.ui.screens.home.RoutineFolder
import com.voltfitness.app.ui.theme.VoltTextPrimary
import com.voltfitness.app.ui.theme.VoltTextSecondary
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * An Organism component that groups the main section title and a collection
 * of horizontal routine lists (folders).
 *
 * @param folders The list of [RoutineFolder] data objects to be rendered.
 * @param onRoutineClick Callback triggered when a specific routine card is pressed.
 * @param onOptionsClick Callback triggered when the options icon on a routine card is pressed.
 * @param onAddRoutineClick Callback triggered when the "Add new Routine" card is pressed,
 *                          passing the folder ID where the new routine should be created.
 * @param modifier The modifier to be applied to the section layout.
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
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = "Routines",
            style = MaterialTheme.typography.titleLarge,
            color = VoltTextPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
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
 * A Molecule component that represents a single folder category.
 * Displays a category header, a horizontal list of routine cards,
 * and an [AddRoutineCard] as the last item for creating new routines.
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        // Category Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_gym_folder),
                contentDescription = null,
                tint = VoltTextSecondary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = VoltTextSecondary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Horizontal Scrollable List
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            routines.forEach { routine ->
                RoutineCard(
                    routine = routine,
                    onCardClick = { onRoutineClick(routine) },
                    onOptionsClick = { onOptionsClick(routine) },
                    modifier = Modifier
                        .width(300.dp)
                        .fillMaxHeight()
                )
            }

            AddRoutineCard(
                onClick = { onAddRoutineClick(folderId) },
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
            )
        }
    }
}


// region Previews

@Preview(name = "Routine Section Preview", showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun RoutineSectionPreview() {
    // Mock data to populate the preview
    val mockFolders = listOf(
        RoutineFolder(
            id = 1,
            name = "My Daily Routines",
            routines = listOf(
                RoutineSummary(1, "Basic Routine", 8, "Chest, Shoulders, Triceps", true),
                RoutineSummary(2, "Recommended ", 7, "Back, Biceps", false),
            )
        ),
        RoutineFolder(
            id = 2,
            name = "Specialized Training",
            routines = listOf(
                RoutineSummary(4, "PPL", 6, "Total body strength", false),
                RoutineSummary(5, "HIIT Blast", 12, "High intensity cardio", false)
            )
        ),
    )

    VoltTheme {
        // Using Surface to ensure the background color from the theme is applied
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            RoutineSection(
                folders = mockFolders,
                onRoutineClick = { routine -> println("Clicked: ${routine.name}") },
                onOptionsClick = { routine -> println("Options for: ${routine.name}") },
                modifier = Modifier.padding(vertical = 16.dp),
                onAddRoutineClick = { folderId -> println("Add new routine for folder: $folderId") }
            )
        }
    }
}

// endregion