package com.voltfitness.app.ui.screens.bodycomposition.add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Straighten
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.voltfitness.app.core.designsystem.component.VoltDateSelector
import com.voltfitness.app.core.designsystem.component.VoltErrorBanner
import com.voltfitness.app.ui.components.navigation.VoltStepBottomBar
import com.voltfitness.app.ui.screens.bodycomposition.add.components.BodyCompositionTab
import com.voltfitness.app.ui.screens.bodycomposition.add.components.BodyMeasurementsTab
import com.voltfitness.app.ui.theme.VoltSpacing
import com.voltfitness.app.ui.theme.VoltTheme
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Screen for recording a body composition snapshot.
 *
 * Uses a tabbed [HorizontalPager] to separate "Body Composition" (scale metrics)
 * from "Body Measurements" (tape metrics).
 *
 * This Composable is stateless; it renders [uiState] and forwards intents via [onEvent].
 * Flow navigation is managed by the parent NavGraph.
 *
 * @param uiState Current form state including validation and saving status.
 * @param onEvent Dispatches user intents to the ViewModel.
 * @param onNavigateBack Callback for the secondary "Dismiss" action.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBodyCompositionScreen(
    uiState: AddBodyCompositionUiState,
    onEvent: (AddBodyCompositionEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }

    val tabs = remember {
        listOf(
            TabItem("Composition", Icons.Outlined.FitnessCenter),
            TabItem("Measurements", Icons.Outlined.Straighten),
        )
    }

    // Contextual logic for the multi-step bottom bar
    val isLastTab = selectedTabIndex.value == tabs.lastIndex
    val primaryText = if (isLastTab) "Save" else "Next"
    val primaryIcon = if (isLastTab) Icons.Filled.Save else Icons.AutoMirrored.Filled.ArrowForward
    val primaryEnabled = if (isLastTab) uiState.isFormValid else true

    Scaffold(
        bottomBar = {
            VoltStepBottomBar(
                primaryText = primaryText,
                primaryIcon = primaryIcon,
                primaryEnabled = primaryEnabled,
                primaryLoading = uiState.isSaving,
                onSecondaryClick = onNavigateBack,
                onPrimaryClick = {
                    if (isLastTab) {
                        onEvent(AddBodyCompositionEvent.Save)
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(selectedTabIndex.value + 1)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding()),
        ) {
            // Error handling at the top of the form
            uiState.errorMessage?.let { error ->
                VoltErrorBanner(
                    message = error,
                    onDismiss = { onEvent(AddBodyCompositionEvent.DismissError) },
                    modifier = Modifier.padding(
                        horizontal = VoltSpacing.medium,
                        vertical = VoltSpacing.small
                    ),
                )
            }

            // Global measurement date selector
            VoltDateSelector(
                label = "Date of measurement",
                selectedDate = uiState.date,
                onDateSelected = { onEvent(AddBodyCompositionEvent.UpdateDate(it)) },
                icon = Icons.Outlined.CalendarMonth,
                modifier = Modifier.padding(horizontal = VoltSpacing.medium),
            )

            // Step indicator/Navigation tabs
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex.value,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) },
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = selectedTabIndex.value == index
                    Tab(
                        selected = isSelected,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            )
                        },
                    )
                }
            }

            // Form Content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                beyondViewportPageCount = 1,
            ) { page ->
                when (page) {
                    0 -> BodyCompositionTab(uiState = uiState, onEvent = onEvent)
                    1 -> BodyMeasurementsTab(uiState = uiState, onEvent = onEvent)
                }
            }
        }
    }
}

/**
 * Metadata for form navigation steps.
 */
private data class TabItem(
    val title: String,
    val icon: ImageVector,
)

// region Previews

@Preview(name = "Form - Initial State", showSystemUi = true)
@Composable
private fun AddBodyCompositionEmptyPreview() {
    VoltTheme {
        AddBodyCompositionScreen(
            uiState = AddBodyCompositionUiState(date = LocalDate.now()),
            onEvent = {},
            onNavigateBack = {},
        )
    }
}

@Preview(name = "Form - Saving state", showSystemUi = true)
@Composable
private fun AddBodyCompositionSavingPreview() {
    VoltTheme {
        AddBodyCompositionScreen(
            uiState = AddBodyCompositionUiState(
                weightKg = "75.5",
                heightCm = "180",
                isSaving = true,
            ),
            onEvent = {},
            onNavigateBack = {},
        )
    }
}

@Preview(name = "Form - Error displayed", showSystemUi = true)
@Composable
private fun AddBodyCompositionErrorPreview() {
    VoltTheme {
        AddBodyCompositionScreen(
            uiState = AddBodyCompositionUiState(
                errorMessage = "Database constraint violation. Please try again.",
            ),
            onEvent = {},
            onNavigateBack = {},
        )
    }
}

// endregion