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
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.voltfitness.app.R
import com.voltfitness.app.ui.screens.body_composition.components.CompositionTab
import com.voltfitness.app.ui.screens.body_composition.components.HistoryTab
import com.voltfitness.app.ui.screens.body_composition.components.MeasurementsTab
import com.voltfitness.app.ui.screens.body_composition.components.QuickSummaryHeader
import kotlinx.coroutines.launch

/**
 * Main Body Composition screen displaying user metrics, measurements, and history.
 *
 * Uses [HorizontalPager] + [TabRow] following the official Android guidelines for
 * tab-based navigation. The pager provides native swipe gestures between tabs and
 * lazy page composition for optimal performance.
 *
 * Tab state is derived from [PagerState.currentPage] using [derivedStateOf] to
 * avoid unnecessary recompositions — the ViewModel no longer needs to track the
 * selected tab index.
 *
 * @param navController Navigation controller for back navigation
 * @param uiState Current UI state from ViewModel
 * @param onAddNewMeasurement Callback to navigate to add measurement screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyCompositionScreen(
    navController: NavController,
    uiState: BodyCompositionUiState,
    onAddNewMeasurement: () -> Unit,
    onEntryClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    var hasAutoRedirected by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading, uiState.historyEntries) {
        if (!uiState.isLoading && uiState.historyEntries.isEmpty() && !hasAutoRedirected) {
            hasAutoRedirected = true
            onAddNewMeasurement()
        }
    }

    val tabs = listOf(
        stringResource(R.string.composition_tab),
        stringResource(R.string.measurements_tab),
        stringResource(R.string.history_tab)
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
                text = { Text(stringResource(R.string.new_measurement)) }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Quick summary header
            uiState.latestEntry?.let { entry ->
                QuickSummaryHeader(
                    userName = uiState.userName,
                    weight = entry.weightKg,
                    bodyFat = entry.bodyFatPercent,
                    muscleMass = entry.muscleMassKg,
                    score = entry.compositionScore,
                    lastDate = entry.date
                )
            }

            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex.value,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) }
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
                                    FontWeight.Bold else FontWeight.Normal
                            )
                        }
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
                    0 -> CompositionTab(entry = uiState.latestEntry)
                    1 -> MeasurementsTab(entry = uiState.latestEntry)
                    2 -> HistoryTab(
                        entries = uiState.historyEntries,
                        onEntryClick = onEntryClick
                    )
                }
            }
        }
    }
}
