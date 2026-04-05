package com.voltfitness.app.ui.navigation

import androidx.compose.runtime.Composable

/**
 * Represents the global configuration for the application's top navigation header.
 *
 * This state holder decouples the screen content from the [VoltTopAppBar] implementation,
 * allowing features to define their own titles, navigation behavior, and
 * contextual action buttons.
 *
 * @property title Primary text header (e.g., screen name or user greeting).
 * @property subtitle Secondary context text (e.g., total workouts or date).
 * @property showBackButton Controls visibility of the [Icons.AutoMirrored.Filled.ArrowBack] icon.
 * @property showSettingsButton Controls visibility of the [Icons.Filled.Settings] icon.
 * @property onBackClick Invoked when the navigation icon is pressed.
 * @property onSettingsClick Invoked when the settings action icon is pressed.
 * @property collapsibleContent Slot for injecting dynamic UI elements (e.g., SearchBar) below the main bar.
 * @property isCollapsibleVisible Lambda used to determine the current display state of the [collapsibleContent].
 */
data class TopAppBarState(
    val title: String = "",
    val subtitle: String? = null,
    val showBackButton: Boolean = false,
    val showSettingsButton: Boolean = false,
    val onBackClick: (() -> Unit)? = null,
    val onSettingsClick: (() -> Unit)? = null,

    // Slot for dynamic components like search fields or filter chips
    val collapsibleContent: (@Composable () -> Unit)? = null,

    // Logic-driven visibility check for the collapsible slot
    val isCollapsibleVisible: () -> Boolean = { true },
)