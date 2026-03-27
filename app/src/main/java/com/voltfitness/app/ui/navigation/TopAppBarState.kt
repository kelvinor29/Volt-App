package com.voltfitness.app.ui.navigation

import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

/**
 * Represents the UI state of the application's top app bar.
 *
 * This data class centralizes the configuration for titles, subtitles,
 * and visibility of navigation/action icons across different screens.
 *
 * @property title The main text displayed in the center/start of the bar.
 * @property subtitle Optional secondary text displayed below the title.
 * @property showBackButton Whether the navigation icon (back arrow) should be visible.
 * @property showSettingsButton Whether the action icon (settings) should be visible.
 * @property onBackClick Callback triggered when the navigation icon is pressed.
 * @property onSettingsClick Callback triggered when the settings icon is pressed.
 */
data class TopAppBarState(
    val title: String = "",
    val subtitle: String? = null,
    val showBackButton: Boolean = false,
    val showSettingsButton: Boolean = false,
    val onBackClick: (() -> Unit)? = null,
    val onSettingsClick: (() -> Unit)? = null,

    val collapsibleContent: (@Composable () -> Unit)? = null,
    val isCollapsibleVisible: () -> Boolean = { true },
)