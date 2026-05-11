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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.R
import com.voltfitness.app.ui.common.UiText
import com.voltfitness.app.ui.navigation.TopAppBarState
import com.voltfitness.app.ui.theme.VoltTheme

/**
 * Global application header that dynamically adapts to the current [TopAppBarState].
 *
 * Features include:
 * - Reactive navigation (Back button visibility).
 * - Contextual metadata (Subtitles for dashboards).
 * - Animated collapsible content (Search bars or Filter chips).
 *
 * Performance note: Uses [derivedStateOf] for [isVisible] to prevent full
 * recompositions when only the collapsible state changes.
 *
 * @param state The reactive state container for the bar's configuration.
 * @param modifier Layout modifiers for the top bar container.
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
                    IconButton(onClick = { state.onBackClick?.invoke() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_back)
                        )
                    }
                }
            },
            title = {
                AppBarTitle(
                    title = state.title.asString(),
                    subtitle = if (!state.showBackButton) state.subtitle.toString() else null
                )
            },
            actions = {
                if (state.showSettingsButton) {
                    IconButton(onClick = { state.onSettingsClick?.invoke() }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = stringResource(R.string.open_settings)
                        )
                    }
                }
            }
        )

        // Collapsible Section (e.g., Search Bar)
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
                    Column {
                        content()
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = colorScheme.outlineVariant
                        )
                    }
                }
            }
        }

        // Bottom border visibility logic
        if (!isVisible) {
            HorizontalDivider(
                thickness = 0.5.dp,
                color = colorScheme.outlineVariant
            )
        }
    }
}

/**
 * Internal title and subtitle layout for the top bar.
 */
@Composable
private fun AppBarTitle(
    title: String,
    subtitle: String?
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// region Previews maintained for UI state validation
@Preview(name = "Dashboard Style")
@Composable
private fun VoltTopAppBarHomePreview() {
    VoltTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            VoltTopAppBar(
                state = TopAppBarState(
                    title = "Hello, Kelvin!" as UiText,
                    subtitle = "Get ready, today is workout day!",
                    showBackButton = false,
                    showSettingsButton = true
                )
            )
        }
    }
}
// endregion

@Preview(name = "Detail Screen Style")
@Composable
private fun VoltTopAppBarDetailPreview() {
    VoltTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            VoltTopAppBar(
                state = TopAppBarState(
                    title = "Push Day Workout" as UiText,
                    showBackButton = true,
                    showSettingsButton = false
                )
            )
        }
    }
}

// endregion