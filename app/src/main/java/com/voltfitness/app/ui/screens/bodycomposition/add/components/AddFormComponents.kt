package com.voltfitness.app.ui.screens.bodycomposition.add.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.voltfitness.app.core.designsystem.component.voltFieldColors
import com.voltfitness.app.core.designsystem.component.voltFieldModifier
import com.voltfitness.app.core.designsystem.component.voltFieldShape
import com.voltfitness.app.core.designsystem.component.voltSectionShape
import com.voltfitness.app.ui.theme.VoltElevation
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * Expandable card section used to group related form fields.
 *
 * Leverages the design system's [voltSectionShape] and [VoltSpacing] to maintain
 * visual consistency with other dashboard containers.
 *
 * @param title Section header text.
 * @param icon Leading icon for the section.
 * @param isExpanded Controls the visibility of the internal [content].
 * @param onToggle Callback to flip the [isExpanded] state.
 * @param content Slot for the specific form fields.
 */
@Composable
fun FormSection(
    title: String,
    icon: ImageVector,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = voltSectionShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = VoltElevation.small)
    ) {
        Column {
            // Interactive Header
            Surface(
                onClick = onToggle,
                color = if (isExpanded)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                else MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(VoltSpacing.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(VoltSpacing.medium))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = if (isExpanded)
                            Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Collapsible Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = VoltSpacing.medium,
                        end = VoltSpacing.medium,
                        bottom = VoltSpacing.medium
                    ),
                    verticalArrangement = Arrangement.spacedBy(VoltSpacing.medium),
                    content = content
                )
            }
        }
    }
}

@Preview()
@Composable
private fun AddFormComponents_DefaultPreview() {
    VoltTheme {
        Column(
            modifier = Modifier.padding(VoltSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(VoltSpacing.medium)
        ) {
            OutlinedTextField(
                value = "75",
                onValueChange = {},
                label = { Text("Weight (kg)") },
                modifier = Modifier.voltFieldModifier(),
                shape = voltFieldShape,
                colors = voltFieldColors()
            )

            OutlinedTextField(
                value = "18",
                onValueChange = {},
                label = { Text("Body Fat (%)") },
                modifier = Modifier.voltFieldModifier(),
                shape = voltFieldShape,
                colors = voltFieldColors()
            )
        }
    }
}

@Preview()
@Composable
private fun AddFormComponents_ErrorPreview() {
    VoltTheme {
        Column(
            modifier = Modifier.padding(VoltSpacing.medium),
            verticalArrangement = Arrangement.spacedBy(VoltSpacing.medium)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Weight (kg)") },
                isError = true,
                supportingText = { Text("Required field") },
                modifier = Modifier.voltFieldModifier(),
                shape = voltFieldShape,
                colors = voltFieldColors(isError = true)
            )
        }
    }
}

@Preview()
@Composable
private fun AddFormComponents_DisabledPreview() {
    VoltTheme {
        OutlinedTextField(
            value = "75",
            onValueChange = {},
            label = { Text("Weight (kg)") },
            enabled = false,
            modifier = Modifier
                .padding(VoltSpacing.medium)
                .voltFieldModifier(),
            shape = voltFieldShape,
            colors = voltFieldColors()
        )
    }
}

@Preview()
@Composable
private fun AddFormComponents_InSectionPreview() {
    VoltTheme {
        FormSection(
            title = "Body Composition",
            icon = Icons.Default.FitnessCenter,
            isExpanded = true,
            onToggle = {}
        ) {
            OutlinedTextField(
                value = "75",
                onValueChange = {},
                label = { Text("Weight (kg)") },
                modifier = Modifier.voltFieldModifier(),
                shape = voltFieldShape,
                colors = voltFieldColors()
            )

            OutlinedTextField(
                value = "18",
                onValueChange = {},
                label = { Text("Body Fat (%)") },
                modifier = Modifier.voltFieldModifier(),
                shape = voltFieldShape,
                colors = voltFieldColors()
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Muscle Mass") },
                isError = true,
                supportingText = { Text("Invalid value") },
                modifier = Modifier.voltFieldModifier(),
                shape = voltFieldShape,
                colors = voltFieldColors(isError = true)
            )
        }
    }
}

@Preview()
@Composable
private fun AddFormComponents_SectionCollapsedPreview() {
    VoltTheme {
        FormSection(
            title = "Body Composition",
            icon = Icons.Default.FitnessCenter,
            isExpanded = false,
            onToggle = {}
        ) {
            // No visible
        }
    }
}