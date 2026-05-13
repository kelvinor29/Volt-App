package com.voltfitness.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voltfitness.app.R
import com.voltfitness.app.domain.model.BodyCompositionEntry
import com.voltfitness.app.domain.model.User
import com.voltfitness.app.domain.repository.RoutineRepository
import com.voltfitness.app.domain.repository.UserRepository
import com.voltfitness.app.domain.usecase.body_composition.GetRecentBodyCompositionsUseCase
import com.voltfitness.app.domain.usecase.exercise.SeedDatabaseUseCase
import com.voltfitness.app.domain.usecase.home.EnsureDefaultFolderUseCase
import com.voltfitness.app.domain.usecase.home.GetUserFoldersWithRoutinesUseCase
import com.voltfitness.app.domain.usecase.user.EvaluateProgressUseCase
import com.voltfitness.app.domain.usecase.user.GetCurrentUserUseCase
import com.voltfitness.app.ui.common.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * ViewModel for the Home screen dashboard.
 *
 * Coordinates multiple data streams:
 * 1. Authenticated user profile.
 * 2. Body composition progress via [EvaluateProgressUseCase].
 * 3. User folders and routine collections.
 *
 * Ensures the database is seeded and a default folder exists upon initialization.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getRecentBodyCompositionsUseCase: GetRecentBodyCompositionsUseCase,
    private val evaluateProgressUseCase: EvaluateProgressUseCase,
    private val getUserFoldersWithRoutinesUseCase: GetUserFoldersWithRoutinesUseCase,
    private val ensureDefaultFolderUseCase: EnsureDefaultFolderUseCase,
    private val seedDatabaseUseCase: SeedDatabaseUseCase,
    private val userRepository: UserRepository,
    private val routineRepository: RoutineRepository,
) : ViewModel() {

    // TODO: Chage uiState to combine all data streams (Reactive MVMM, unidirectional data flow)
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        initializeDashboard()
    }

    /**
     * Orchestrates the initial setup: seeding data and starting primary observations.
     */
    private fun initializeDashboard() {
        viewModelScope.launch {
            // Ensure base data exists
            seedDatabaseUseCase()

            val userId = userRepository.getCurrentUserId() ?: return@launch

            // Maintenance: Ensure user has at least one folder
            ensureDefaultFolderUseCase(userId)

            // Start reactive observations
            observeUserAndCompositions()
            observeFolders(userId)
        }
    }

    /**
     * Observes the current user and triggers composition tracking upon profile availability.
     */
    private fun observeUserAndCompositions() {
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
     * Collects routine folders for the specific user.
     */
    private fun observeFolders(userId: Long) {
        viewModelScope.launch {
            getUserFoldersWithRoutinesUseCase(userId)
                .collect { folders ->
                    _uiState.update { it.copy(folders = folders) }
                }
        }
    }

    /**
     * Monitors recent body metrics to calculate progress trends.
     */
    private fun observeBodyCompositions(user: User) {
        viewModelScope.launch {
            getRecentBodyCompositionsUseCase(user.id, limit = 2)
                .collect { entries ->
                    if (entries.isNotEmpty()) {
                        handleCompositionProgress(entries, user)
                    }
                }
        }
    }


    /**
     * Observes the active routine days and updates the UI state accordingly.
     */
    private fun observeActiveRoutineDays() {
        viewModelScope.launch {
            activeDaysFlow.collect { days ->
                _uiState.update { it.copy(activeRoutineDays = days) }
            }
        }
    }

    /**
     * Transforms raw database entries into formatted UI strings for the dashboard cards.
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
                secondaryChangeText = evaluation.secondaryText?.let { text ->
                    text
                },
                progressStatus = evaluation.status,
                compositionScore = latest.compositionScore
            )
        }
    }

    /**
     * Merges the active-routine-days stream into the main [uiState] using [combine].
     * This avoids a second StateFlow and keeps the UI driven by a single source of truth.
     */
    private val activeDaysFlow: Flow<List<ActiveRoutineDayUi>> =
        routineRepository.getActiveRoutineDays()
            .map { entities ->
                entities.map { entity ->
                    ActiveRoutineDayUi(
                        dayId = entity.dayId,
                        dayOrder = entity.dayOrder,
                        name = entity.name,
                        focusBodyParts = entity.focusBodyParts.orEmpty()
                    )
                }
            }
            .catch { emit(emptyList()) }

    /**
     * Formats a [LocalDate] into a user-friendly relative duration string.
     */
    private fun formatRelativeDate(date: LocalDate): UiText {
        val daysAgo = ChronoUnit.DAYS.between(date, LocalDate.now())
        return when {
            daysAgo == 0L -> UiText.StringResource(R.string.updated_today)
            daysAgo == 1L -> UiText.StringResource(R.string.updated_yesterday)
            daysAgo in 2..6 -> UiText.StringResource(R.string.updated_days_ago, daysAgo)
            daysAgo < 30 -> UiText.StringResource(R.string.updated_weeks_ago, daysAgo / 7)
            else -> UiText.StringResource(R.string.updated_months_ago, daysAgo / 30)
        }
    }
}