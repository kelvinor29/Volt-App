package com.voltfitness.app.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoltDateSelector(
    label: String,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.CalendarMonth,
    dateFormat: String = "dd MMM yyyy",
    placeholder: String = "Not set",
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var showPicker by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern(dateFormat)
    val dateText = selectedDate?.format(formatter) ?: ""

    if (showPicker) {
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate?.toEpochDay()?.times(86_400_000L)
        )
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(LocalDate.ofEpochDay(millis / 86_400_000L))
                    }
                    showPicker = false
                }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showPicker = true }
    ) {
        OutlinedTextField(
            value = dateText,
            onValueChange = {},
            enabled = false,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            isError = isError,
            supportingText = {
                if (isError && errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = if (isError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLeadingIconColor = if (isError) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.primary,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun VoltDateSelectorPreview() {
    VoltTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                VoltDateSelector(
                    label = "Date of Birth",
                    selectedDate = null,
                    onDateSelected = {},
                    placeholder = "Select your birthday"
                )

                VoltDateSelector(
                    label = "Workout Date",
                    selectedDate = LocalDate.now(),
                    onDateSelected = {},
                    icon = Icons.Outlined.Event
                )

                VoltDateSelector(
                    label = "Last Assessment",
                    selectedDate = LocalDate.of(2023, 12, 25),
                    onDateSelected = {},
                    dateFormat = "MM/dd/yy",
                    icon = Icons.Outlined.History
                )
            }
        }
    }
}