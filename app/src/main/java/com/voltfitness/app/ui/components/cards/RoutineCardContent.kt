package com.voltfitness.app.ui.components.cards

import com.voltfitness.app.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.ui.components.VoltCard
import com.voltfitness.app.ui.theme.VoltGradientStart
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * Data representation of a routine for UI display.
 *
 * @property id Unique identifier for the routine.
 * @property name The title of the workout plan.
 * @property daysCount Number of training sessions per week.
 * @property description Brief summary of the routine's focus.
 * @property isMainRoutine Whether this is the user's currently active/primary routine.
 */
data class RoutineSummary(
    val id: Long,
    val name: String,
    val daysCount: Int,
    val description: String,
    val isMainRoutine: Boolean = false
)

/**
 * A clickable card component that displays summary information about a gym routine.
 * Designed to be used within a list of routines.
 *
 * @param routine The [RoutineSummary] data to display.
 * @param onCardClick Callback invoked when the user taps on the card to see details.
 * @param onOptionsClick Callback invoked when the user taps the options/more icon.
 * @param modifier Modifier to be applied to the card.
 */
@Composable
fun RoutineCard(
    routine: RoutineSummary,
    onCardClick: () -> Unit,
    onOptionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    VoltCard(
        onClick = onCardClick,
        modifier = modifier.fillMaxWidth(),
    ) {
        RoutineCardContent(
            routine = routine,
            onOptionsClick = onOptionsClick
        )
    }
}

/**
 * Internal content for the [RoutineCard].
 * Handles the layout of the logo, title, days count, and description.
 */
@Composable
private fun RoutineCardContent(
    routine: RoutineSummary,
    onOptionsClick: () -> Unit
) {
    // Grayscale filter logic: Active routine shows colors, inactive is desaturated
    val iconColorFilter = if (routine.isMainRoutine) null
    else ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) })

    // Visual indicator for the main routine (optional subtle border)
    val logoModifier = if (routine.isMainRoutine)
        Modifier.border(1.dp, VoltGradientStart, CircleShape)
    else Modifier

    Column(
        modifier = Modifier
            .padding(VoltSpacing.medium)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Elevation/Shadow container for the logo
            Surface(
                shape = CircleShape,
                shadowElevation = if (routine.isMainRoutine) 4.dp else 1.dp,
                modifier = Modifier
                    .size(36.dp)
                    .then(logoModifier)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_volt_logo),
                    contentDescription = if (routine.isMainRoutine) {
                        stringResource(R.string.active_main_routine)
                    } else stringResource(R.string.inactive_routine),
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(if (routine.isMainRoutine) 1f else 0.7f),
                    colorFilter = iconColorFilter,
                    contentScale = Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Title and Action Area
            Text(
                text = routine.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onOptionsClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.open_routine_options),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Frequency Label
        Text(
            text = stringResource(R.string.days_a_week, routine.daysCount),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (routine.isMainRoutine) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Routine Description
        Text(
            text = routine.description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = MaterialTheme.typography.bodySmall.lineHeight * 1.2 // Better readability
        )
    }
}


@Preview
@Composable
private fun RoutineCardPreview() {
    val mainRoutine = RoutineSummary(
        id = 1,
        name = "Advanced Hypertrophy",
        daysCount = 5,
        description = "Routine focused on gaining muscle mass with high frequency training and progressive overload.",
        isMainRoutine = true
    )

    val secondaryRoutine = RoutineSummary(
        id = 2,
        name = "Basic Strength",
        daysCount = 3,
        description = "Perfect for beginners looking to build a strong foundation in compound movements.",
        isMainRoutine = false
    )

    VoltTheme {
        Column(
            modifier = Modifier
                                        .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RoutineCard(routine = mainRoutine, onCardClick = {}, onOptionsClick = {})
            RoutineCard(routine = secondaryRoutine, onCardClick = {}, onOptionsClick = {})
        }
    }
}