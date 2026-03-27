package com.voltfitness.app.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.voltfitness.app.ui.theme.VoltTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun VoltErrorBanner(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Warning,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Dismiss error"
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
private fun VoltErrorBannerPreview() {
    VoltTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // Standard Short Error
                VoltErrorBanner(
                    message = "Failed to sync your workout data.",
                    onDismiss = {}
                )

                // Long Error Message (Testing Text Wrap)
                VoltErrorBanner(
                    message = "A connection timeout occurred while attempting to reach the ExerciseDB server. Please verify your internet settings and try again.",
                    onDismiss = {}
                )

                // Simple Authentication Error
                VoltErrorBanner(
                    message = "Invalid credentials. Please sign in again.",
                    onDismiss = {}
                )
            }
        }
    }
}

@Preview(name = "Banner in Context", showBackground = true)
@Composable
private fun VoltErrorBannerContextPreview() {
    VoltTheme {
        Column(modifier = Modifier.padding(8.dp)) {
            VoltErrorBanner(
                message = "Limited connectivity detected.",
                onDismiss = {}
            )
        }
    }
}