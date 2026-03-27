package com.voltfitness.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voltfitness.app.domain.model.BodyCompositionEntry
import com.voltfitness.app.domain.model.User
import com.voltfitness.app.domain.repository.UserRepository
import com.voltfitness.app.domain.usecase.body_composition.GetRecentBodyCompositionsUseCase
import com.voltfitness.app.domain.usecase.exercise.SeedDatabaseUseCase
import com.voltfitness.app.domain.usecase.home.EnsureDefaultFolderUseCase
import com.voltfitness.app.domain.usecase.home.GetUserFoldersWithRoutinesUseCase
import com.voltfitness.app.domain.usecase.user.EvaluateProgressUseCase
import com.voltfitness.app.domain.usecase.user.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * ViewModel for the Home screen dashboard.
 *
 * Observes the current user and their two most recent body composition entries,
 * then delegates progress evaluation to [EvaluateProgressUseCase]. The resulting
 * [HomeUiState] contains pre-formatted strings consumed directly by [WeightCard].
 *
 * Navigation is handled by the UI layer — this ViewModel exposes no navigation events.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getRecentBodyCompositionsUseCase: GetRecentBodyCompositionsUseCase,
    private val evaluateProgressUseCase: EvaluateProgressUseCase,
    private val getUserFoldersWithRoutinesUseCase: GetUserFoldersWithRoutinesUseCase,
    private val ensureDefaultFolderUseCase: EnsureDefaultFolderUseCase,
    private val seedDatabaseUseCase: SeedDatabaseUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeDashboardData()
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId()

            if (userId == null) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            seedDatabaseUseCase()

//            ensureDefaultFolderUseCase(userId = userId)

            getUserFoldersWithRoutinesUseCase(userId = userId)
                .collect { folders ->
                    _uiState.update { it.copy(folders = folders) }
                }
        }
    }


    /**
     * Observes the current user and, once available, starts observing
     * their recent body composition entries for the weight card.
     */
    private fun observeDashboardData() {
        viewModelScope.launch {
            getCurrentUserUseCase()
                .filterNotNull()
                .collect { user ->
                    _uiState.update { it.copy(user = user, isLoading = false) }
                    observeBodyCompositions(user)
                }
        }
    }

    /**
     * Collects the two most recent body composition entries and updates
     * the weight card state accordingly.
     */
    private fun observeBodyCompositions(user: User) {
        viewModelScope.launch {
            getRecentBodyCompositionsUseCase(user.id, limit = 2)
                .collect { entries ->
                    if (!entries.isEmpty()) handleCompositionProgress(entries, user)
                }
        }
    }


    /**
     * Evaluates progress between the latest and previous entries,
     * formatting the results for [WeightCard] display.
     */
    private fun handleCompositionProgress(
        entries: List<BodyCompositionEntry>,
        user: User
    ) {
        val latest = entries.first()
        val previous = entries.getOrNull(1)

        val evaluation = evaluateProgressUseCase(
            current = latest,
            previous = previous,
            userGoal = user.goal
        )

        _uiState.update {
            it.copy(
                currentWeight = "%.1f".format(latest.weightKg),
                lastWeightUpdated = formatRelativeDate(latest.date),
                primaryChangeText = evaluation.primaryText,
                secondaryChangeText = evaluation.secondaryText,
                progressStatus = evaluation.status,
                compositionScore = latest.compositionScore
            )
        }
    }

    /**
     * Converts a [LocalDate] into a human-readable relative time label.
     *
     * Examples: "Updated today", "Updated yesterday", "Updated 3 days ago",
     * "Updated 2 weeks ago", "Updated 1 month ago".
     */
    private fun formatRelativeDate(date: LocalDate): String {
        val daysAgo = ChronoUnit.DAYS.between(date, LocalDate.now())
        return when {
            daysAgo == 0L -> "Updated today"
            daysAgo == 1L -> "Updated yesterday"
            daysAgo in 2..6 -> "Updated $daysAgo days ago"
            daysAgo < 30 -> "Updated ${daysAgo / 7} weeks ago"
            else -> "Updated ${daysAgo / 30} months ago"
        }
    }
}
