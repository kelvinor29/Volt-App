package com.voltfitness.app.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.ui.theme.VoltTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoltDropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    leadingIcon: ImageVector? = null,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = it },
        modifier = modifier.voltFieldModifier()
    ) {
        OutlinedTextField(
            value = selectedOption.ifBlank { "" },
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = if (isError) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = enabled
                ),
            shape = MaterialTheme.shapes.medium,
            enabled = enabled,
            colors = voltFieldColors(),
            supportingText = {
                if (isError && errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            isError = isError,
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun VoltDropdownSelectorPreview() {
    val difficultyLevels = listOf("Beginner", "Intermediate", "Advanced", "Professional")
    val goals = listOf("Build Muscle", "Lose Weight", "Improve Endurance", "Stay Fit")

    VoltTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                VoltDropdownSelector(
                    label = "Fitness Level",
                    options = difficultyLevels,
                    selectedOption = "",
                    onOptionSelected = {}
                )

                VoltDropdownSelector(
                    label = "Main Goal",
                    options = goals,
                    selectedOption = "Build Muscle",
                    onOptionSelected = {}
                )

                VoltDropdownSelector(
                    label = "Rest Time Between Sets",
                    options = listOf("30 seconds", "60 seconds", "90 seconds"),
                    selectedOption = "60 seconds",
                    onOptionSelected = {},
                    enabled = false
                )
            }
        }
    }
}