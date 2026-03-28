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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.ui.navigation.TopAppBarState
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * This component reacts to the [TopAppBarState] to dynamically show navigation
 * buttons, titles, and action items. All icons and text follow the theme's
 * high-contrast colors (white in dark mode).
 *
 * ### Collapse behavior
 * The [TopAppBarState.isCollapsibleVisible] lambda is read via [derivedStateOf]
 * so that only the AnimatedVisibility block re-composes when visibility changes,
 * not the entire VoltTopAppBar tree. [TopAppBarState] must be annotated with
 * [@Stable][androidx.compose.runtime.Stable] for this optimization to take effect
 * (see TopAppBarState.kt).
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
    val colorScheme = MaterialTheme.colorScheme

    val appBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = colorScheme.background,
        titleContentColor = colorScheme.onBackground,
        navigationIconContentColor = colorScheme.onBackground,
        actionIconContentColor = colorScheme.onBackground
    )

    val isVisible by remember(state) {
        derivedStateOf { state.isCollapsibleVisible() }
    }

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
                                color = colorScheme.onSurfaceVariant,
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
                visible = isVisible,
                enter = expandVertically(
                    animationSpec = tween(durationMillis = 200, easing = EaseInOutCubic)
                ),
                exit = shrinkVertically(
                    shrinkTowards = Alignment.Top,
                    animationSpec = tween(durationMillis = 150, easing = EaseInOutCubic)
                )
            ) {
                Surface(
                    color = colorScheme.background,
                    shadowElevation = 4.dp
                ) {
                    content()
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = colorScheme.outlineVariant
                    )
                }
            }
        }

        if (!isVisible) {
            HorizontalDivider(
                thickness = 0.5.dp,
                color = colorScheme.outlineVariant
            )
        }
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