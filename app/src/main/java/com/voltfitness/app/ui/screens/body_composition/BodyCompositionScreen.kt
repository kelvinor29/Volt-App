package com.voltfitness.app.ui.screens.body_composition

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
import com.voltfitness.app.ui.screens.body_composition.components.CompositionTab
import com.voltfitness.app.ui.screens.body_composition.components.HistoryTab
import com.voltfitness.app.ui.screens.body_composition.components.MeasurementsTab
import com.voltfitness.app.ui.screens.body_composition.components.QuickSummaryHeader
import kotlinx.coroutines.launch

/**
 * Main Body Composition screen displaying user metrics, measurements, and history.
 *
 * This Composable is fully stateless — it only renders [uiState] and
 * forwards user intents via callbacks. Navigation side-effects
 * (e.g. the first-launch redirect) are handled upstream in the NavGraph,
 * which observes [BodyCompositionViewModel.navigationEffect].
 *
 * @param uiState Current UI state from [BodyCompositionViewModel].
 * @param onAddNewMeasurement Callback to navigate to the Add screen.
 * @param onEntryClick Callback when a history entry is tapped.
 * @param modifier Optional layout modifier.
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
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding()),
        ) {
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

            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex.value,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) },
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex.value == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex.value == index)
                                    FontWeight.Bold else FontWeight.Normal,
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