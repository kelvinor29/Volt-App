package com.voltfitness.app.ui.screens.bodycomposition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voltfitness.app.domain.model.BodyCompositionEntry
import com.voltfitness.app.domain.repository.BodyCompositionRepository
import com.voltfitness.app.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Orchestrates data for the [BodyCompositionScreen].
 *
 * Responsibilities:
 * - Reactive loading of user profile and historical body metrics.
 * - Automatic redirection to onboarding ([NavigateToAdd]) for first-time users.
 * - Transformation of domain entities into UI-optimized display models.
 */
@HiltViewModel
class BodyCompositionViewModel @Inject constructor(
    private val bodyCompositionRepository: BodyCompositionRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BodyCompositionUiState())
    val uiState: StateFlow<BodyCompositionUiState> = _uiState.asStateFlow()

    /**
     * One-shot navigation events consumed as side-effects by the UI layer.
     * Uses a [SharedFlow] with no replay to prevent event re-triggering on configuration changes.
     */
    private val _navigationEffect = MutableSharedFlow<BodyCompositionNavEffect>(replay = 0)
    val navigationEffect: SharedFlow<BodyCompositionNavEffect> = _navigationEffect.asSharedFlow()

    /** Guard to ensure the "no-data" redirect logic only executes once per session. */
    private var redirectHandled = false

    init {
        observeBodyCompositionData()
    }

    // ─── Data Orchestration ─────────────────────────────────────────────────

    /**
     * Connects to domain repositories to establish a reactive data stream.
     * Combines user metadata with composition history.
     */
    private fun observeBodyCompositionData() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId()
            if (userId == null) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            val user = userRepository.getUserById(userId)

            // Combine history stream with the existence check for first-launch logic
            combine(
                bodyCompositionRepository.getRecentEntries(userId, limit = 50),
                bodyCompositionRepository.hasEntries(userId),
            ) { entries, hasEntries ->
                entries to hasEntries
            }.collect { (entries, hasEntries) ->
                val displayItems = entries.map { it.toDisplayItem() }

                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        userName = user?.name ?: "User",
                        gender = user?.gender,
                        goal = user?.goal,
                        latestEntry = displayItems.firstOrNull(),
                        historyEntries = displayItems,
                    )
                }

                handleFirstLaunchRedirect(hasEntries)
            }
        }
    }

    /**
     * Triggers navigation to the entry form if the user profile is empty.
     */
    private suspend fun handleFirstLaunchRedirect(hasEntries: Boolean) {
        if (!hasEntries && !redirectHandled) {
            redirectHandled = true
            _navigationEffect.emit(BodyCompositionNavEffect.NavigateToAdd)
        }
    }

    // ─── Internal Mappers ───────────────────────────────────────────────────

    /**
     * Local transformation from Domain Entity to UI Display Model.
     */
    private fun BodyCompositionEntry.toDisplayItem() = BodyCompositionDisplayItem(
        entryId = entryId,
        date = date,
        weightKg = weightKg,
        bodyFatPercent = bodyFatPercent,
        waterPercent = waterPercent,
        muscleMassKg = muscleMassKg,
        visceralFatPercent = visceralFatPercent,
        basalCalories = basalCalories,
        metabolicAge = metabolicAge,
        boneMassKg = boneMassKg,
        chestCm = chestCm,
        waistCm = waistCm,
        hipCm = hipCm,
        gluteCm = gluteCm,
        leftArmCm = leftArmCm,
        rightArmCm = rightArmCm,
        leftLegCm = leftLegCm,
        rightLegCm = rightLegCm,
        fatMassKg = fatMassKg,
        leanMassKg = leanMassKg,
        ffmi = ffmi,
        waistHipRatio = waistHipRatio,
        compositionScore = compositionScore,
    )
}

/**
 * Defines unique navigation intents for the Body Composition flow.
 */
sealed interface BodyCompositionNavEffect {
    /** Signals that the user must be redirected to the measurement form. */
    data object NavigateToAdd : BodyCompositionNavEffect
}