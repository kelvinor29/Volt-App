package com.voltfitness.app.ui.screens.body_composition.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.voltfitness.app.core.designsystem.component.VoltButton
import com.voltfitness.app.core.designsystem.component.VoltDateSelector
import com.voltfitness.app.core.designsystem.component.VoltErrorBanner
import com.voltfitness.app.core.designsystem.component.VoltOutlinedButton
import com.voltfitness.app.ui.screens.body_composition.add.components.BodyCompositionTab
import com.voltfitness.app.ui.screens.body_composition.add.components.BodyMeasurementsTab
import com.voltfitness.app.ui.theme.VoltTheme
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Screen for recording a body composition snapshot.
 *
 * Uses [PrimaryTabRow] + [HorizontalPager] to split inputs into
 * "Body Composition" (scale metrics) and "Body Measurements" (tape metrics).
 *
 * Tab state lives in Compose via [PagerState] — NOT in the ViewModel.
 * All form data persists in the ViewModel's StateFlow, so switching
 * tabs never loses user input.
 *
 * @param uiState Current form state from ViewModel.
 * @param onEvent Dispatches form events to ViewModel.
 * @param onNavigateBack Callback to pop this screen off the navigation stack.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBodyCompositionScreen(
    uiState: AddBodyCompositionUiState,
    onEvent: (AddBodyCompositionEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })
    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }

    val tabs = listOf(
        TabItem("Composition", Icons.Outlined.FitnessCenter),
        TabItem("Measurements", Icons.Outlined.Straighten)
    )

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) onNavigateBack()
    }

    Scaffold(
        bottomBar = {
            Surface(
                shadowElevation = 10.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                val isLastTab = selectedTabIndex.value == tabs.lastIndex

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 10.dp, top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    VoltOutlinedButton(
                        text = "Dismiss",
                        onClick = onNavigateBack,
                        modifier = Modifier.weight(1f)
                    )
                    VoltButton(
                        text = if (isLastTab) "Save" else "Next",
                        icon = if (isLastTab) Icons.Filled.Save else Icons.AutoMirrored.Filled.ArrowForward,
                        onClick = {
                            if (isLastTab)
                                onEvent(AddBodyCompositionEvent.Save)
                            else {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(selectedTabIndex.value + 1)
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = if (isLastTab) uiState.isFormValid else true,
                        isLoading = uiState.isSaving,
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Error Banner
            uiState.errorMessage?.let { error ->
                VoltErrorBanner(
                    message = error,
                    onDismiss = { onEvent(AddBodyCompositionEvent.DismissError) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Date Selector
            VoltDateSelector(
                label = "Date of measurement",
                selectedDate = uiState.date,
                onDateSelected = { onEvent(AddBodyCompositionEvent.UpdateDate(it)) },
                icon = Icons.Outlined.CalendarMonth,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex.value,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) }
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTabIndex.value == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = tab.title,
                                fontWeight = if (selectedTabIndex.value == index)
                                    FontWeight.Bold else FontWeight.Normal
                            )
                        },
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                beyondViewportPageCount = 1
            ) { page ->
                when (page) {
                    0 -> BodyCompositionTab(uiState = uiState, onEvent = onEvent)
                    1 -> BodyMeasurementsTab(uiState = uiState, onEvent = onEvent)
                }
            }
        }
    }
}

/** Simple data holder for tab metadata. */
private data class TabItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)


@Preview(name = "Add Screen - Form Empty", showSystemUi = true)
@Composable
private fun AddBodyCompositionEmptyPreview() {
    VoltTheme {
        AddBodyCompositionScreen(
            uiState = AddBodyCompositionUiState(
                date = LocalDate.now(),
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}

@Preview(name = "Add Screen - Error & Saving", showSystemUi = true)
@Composable
private fun AddBodyCompositionErrorPreview() {
    VoltTheme {
        AddBodyCompositionScreen(
            uiState = AddBodyCompositionUiState(
                errorMessage = "Connection timeout. Please check your internet.",
                isSaving = true,
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}
