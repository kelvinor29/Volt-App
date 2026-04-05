package com.voltfitness.app.ui.screens.bodycomposition

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.voltfitness.app.R
import com.voltfitness.app.core.designsystem.component.voltFieldShape
import com.voltfitness.app.ui.screens.bodycomposition.components.CompositionTab
import com.voltfitness.app.ui.screens.bodycomposition.components.HistoryTab
import com.voltfitness.app.ui.screens.bodycomposition.components.MeasurementsTab
import com.voltfitness.app.ui.screens.bodycomposition.components.QuickSummaryHeader
import kotlinx.coroutines.launch

/**
 * Main Body Composition hub that aggregates health metrics into a tabbed interface.
 *
 * This screen is stateless; it consumes [uiState] and emits events via callbacks.
 * It leverages [voltFieldShape] for selection components to maintain design consistency.
 *
 * Layout structure:
 * 1. [QuickSummaryHeader]: Highlights key metrics (Weight, Body Fat, Muscle).
 * 2. [PrimaryTabRow]: Navigation between Composition, Measurements, and History.
 * 3. [HorizontalPager]: Swipable content area for each category.
 *
 * @param uiState Reactive UI state containing user data and history.
 * @param onAddNewMeasurement Action trigger for the new entry form.
 * @param onEntryClick Action trigger for viewing specific historical record details.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyCompositionScreen(
    uiState: BodyCompositionUiState,
    onAddNewMeasurement: () -> Unit,
    onEntryClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(
        stringResource(R.string.composition_tab),
        stringResource(R.string.measurements_tab),
        stringResource(R.string.history_tab),
    )

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddNewMeasurement,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text(stringResource(R.string.new_measurement)) },
                shape = voltFieldShape
            )
        },
    ) { innerPadding ->

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Displays the most recent summary data at the top
            uiState.latestEntry?.let { entry ->
                QuickSummaryHeader(
                    userName = uiState.userName,
                    weight = entry.weightKg,
                    bodyFat = entry.bodyFatPercent,
                    muscleMass = entry.muscleMassKg,
                    score = entry.compositionScore,
                    lastDate = entry.date,
                )
            }

            // Tab navigation synchronized with the Pager
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex.value,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                },
            ) {
                tabs.forEachIndexed { index, title ->
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
                                text = title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            )
                        },
                    )
                }
            }

            // Content area with swipe gesture support
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                beyondViewportPageCount = 1,
            ) { page ->
                when (page) {
                    0 -> CompositionTab(entry = uiState.latestEntry)
                    1 -> MeasurementsTab(entry = uiState.latestEntry)
                    2 -> HistoryTab(
                        entries = uiState.historyEntries,
                        onEntryClick = onEntryClick,
                    )
                }
            }
        }
    }
}