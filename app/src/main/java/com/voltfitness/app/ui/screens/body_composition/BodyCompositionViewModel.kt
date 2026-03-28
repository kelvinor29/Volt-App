package com.voltfitness.app.ui.screens.body_composition

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
 * ViewModel for [BodyCompositionScreen].
 *
 * Single responsibility: loading and exposing the user's body composition
 * history. It also emits a one-shot [navigationEffect] to redirect first-time
 * users to [AddBodyCompositionScreen] without leaking navigation logic into the
 * Composable.
 *
 * The redirect fires only once — guarded by [redirectHandled] — so rotating
 * the device or recomposing never triggers a duplicate navigation.
 */
@HiltViewModel
class BodyCompositionViewModel @Inject constructor(
    private val bodyCompositionRepository: BodyCompositionRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(BodyCompositionUiState())
    val uiState: StateFlow<BodyCompositionUiState> = _uiState.asStateFlow()

    /**
     * One-shot navigation events consumed by the NavGraph host.
     * Using [MutableSharedFlow] with replay = 0 ensures the event is
     * delivered exactly once even across recompositions.
     */
    private val _navigationEffect = MutableSharedFlow<BodyCompositionNavEffect>(replay = 0)
    val navigationEffect: SharedFlow<BodyCompositionNavEffect> = _navigationEffect.asSharedFlow()

    /** Prevents the first-launch redirect from firing more than once. */
    private var redirectHandled = false

    init {
        observeData()
    }

    // ─── Data Loading ───────────────────────────────────────────────────────

    private fun observeData() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId()
            if (userId == null) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            val user = userRepository.getUserById(userId)

            combine(
                bodyCompositionRepository.getRecentEntries(userId, limit = 50),
                bodyCompositionRepository.hasEntries(userId),
            ) { entries, hasEntries ->
                Pair(entries, hasEntries)
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

                // Emit redirect only once, and only after we are sure there are no entries.
                if (!hasEntries && !redirectHandled) {
                    redirectHandled = true
                    _navigationEffect.emit(BodyCompositionNavEffect.NavigateToAdd)
                }
            }
        }
    }

    // ─── Mapping ────────────────────────────────────────────────────────────

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
 * One-shot navigation effects emitted by [BodyCompositionViewModel].
 */
sealed interface BodyCompositionNavEffect {
    /** Redirect the user to the Add screen because they have no entries yet. */
    data object NavigateToAdd : BodyCompositionNavEffect
}