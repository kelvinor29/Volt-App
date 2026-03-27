package com.voltfitness.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.ui.navigation.TopAppBarState
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * A customized Material 3 TopAppBar for the VoltFitness application.
 *
 * This component reacts to the [TopAppBarState] to dynamically show navigation
 * buttons, titles, and action items. All icons and text follow the theme's
 * high-contrast colors (white in dark mode).
 *
 * @param state The UI state containing title, subtitle, and button configurations.
 * @param modifier Modifier to be applied to the top app bar layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoltTopAppBar(
    state: TopAppBarState,
    modifier: Modifier = Modifier
) {
    // Define shared color configuration to maintain consistency across the app
    val appBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
        actionIconContentColor = MaterialTheme.colorScheme.onBackground
    )

    Column(modifier = modifier) {
        TopAppBar(
            colors = appBarColors,
            navigationIcon = {
                if (state.showBackButton) {
                    IconButton(onClick = state.onBackClick ?: {}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            },
            title = {
                Column {
                    Text(
                        text = state.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!state.showBackButton) {
                        state.subtitle?.let { subtitle ->
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            },
            actions = {
                if (state.showSettingsButton) {
                    IconButton(onClick = { state.onSettingsClick?.invoke() }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Open settings"
                        )
                    }
                }
            }
        )

        state.collapsibleContent?.let { content ->
            AnimatedVisibility(
                visible = state.isCollapsibleVisible(),
                enter = expandVertically(
                    animationSpec = tween(durationMillis = 300, easing = EaseInOutCubic)
                ),
                exit = shrinkVertically(
                    animationSpec = tween(durationMillis = 250, easing = EaseInOutCubic)
                )
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    shadowElevation = 4.dp
                ) {
                    content()
                }
            }
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

// region Previews

@Preview(name = "Home Screen Style")
@Composable
private fun VoltTopAppBarHomePreview() {
    VoltTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            VoltTopAppBar(
                state = TopAppBarState(
                    title = "Hello, Kelvin!",
                    subtitle = "Get ready, today is workout day!",
                    showBackButton = false,
                    showSettingsButton = true
                )
            )
        }
    }
}

@Preview(name = "Detail Screen Style")
@Composable
private fun VoltTopAppBarDetailPreview() {
    VoltTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            VoltTopAppBar(
                state = TopAppBarState(
                    title = "Push Day Workout",
                    showBackButton = true,
                    showSettingsButton = false
                )
            )
        }
    }
}

// endregion