package com.voltfitness.app.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.ui.theme.VoltTheme

@Composable
fun VoltMeasurementField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    suffix: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    imeAction: ImeAction = ImeAction.Next,
    isError: Boolean = false,
    isDecimal: Boolean = true
) {
    VoltTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier,
        leadingIcon = icon,
        suffix = suffix,
        isError = isError,
        imeAction = imeAction,
        isDecimal = isDecimal
    )
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun VoltMeasurementFieldPreview() {
    VoltTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                VoltMeasurementField(
                    value = "75.5",
                    onValueChange = {},
                    label = "Body Weight",
                    suffix = "kg",
                    icon = Icons.Default.MonitorWeight
                )

                VoltMeasurementField(
                    value = "180",
                    onValueChange = {},
                    label = "Height",
                    suffix = "cm",
                    icon = Icons.Default.Height,
                    isDecimal = false
                )

                VoltMeasurementField(
                    value = "invalid",
                    onValueChange = {},
                    label = "Body Fat",
                    suffix = "%",
                    icon = Icons.Default.Percent,
                    isError = true
                )

                VoltMeasurementField(
                    value = "",
                    onValueChange = {},
                    label = "Chest Circumference",
                    suffix = "in"
                )
            }
        }
    }
}