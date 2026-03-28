package com.voltfitness.app.ui.screens.body_composition.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voltfitness.app.domain.repository.UserRepository
import com.voltfitness.app.domain.usecase.body_composition.RawBodyCompositionInput
import com.voltfitness.app.domain.usecase.body_composition.SaveBodyCompositionEntryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel exclusively for [AddBodyCompositionScreen].
 *
 * Owns the form state, validates input, and delegates persistence to
 * [SaveBodyCompositionEntryUseCase]. Navigation side-effects are communicated
 * through [navigationEffect] so the Composable stays stateless.
 *
 * Key performance decisions:
 * - User data is loaded once in [init] and cached; no re-fetch on save.
 * - After a successful save, [SaveBodyCompositionEntryUseCase] writes to the
 *   DB and Room's Flow in [BodyCompositionViewModel] auto-updates — no manual
 *   reload is needed here.
 * - [isSaving] flips to `true` immediately on [AddBodyCompositionEvent.Save],
 *   giving instant visual feedback before the suspend call completes.
 */
@HiltViewModel
class AddBodyCompositionViewModel @Inject constructor(
    private val saveBodyCompositionEntry: SaveBodyCompositionEntryUseCase,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBodyCompositionUiState())
    val uiState: StateFlow<AddBodyCompositionUiState> = _uiState.asStateFlow()

    /**
     * One-shot effects consumed by the NavGraph host.
     * [AddBodyCompositionNavEffect.SavedSuccessfully] signals the host to
     * pop back, keeping nav logic out of the Composable.
     */
    private val _navigationEffect = MutableSharedFlow<AddBodyCompositionNavEffect>(replay = 0)
    val navigationEffect: SharedFlow<AddBodyCompositionNavEffect> = _navigationEffect.asSharedFlow()

    // Pre-loaded on VM creation to avoid a DB round-trip on every Save tap.
    private var cachedUserId: Long? = null
    private var cachedGender: String? = null
    private var cachedBirthDate: LocalDate? = null

    init {
        prefetchUserData()
    }

    // ─── Pre-fetch ──────────────────────────────────────────────────────────

    private fun prefetchUserData() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId() ?: return@launch
            val user = userRepository.getUserById(userId) ?: return@launch

            cachedUserId = user.id
            cachedGender = user.gender
            cachedBirthDate = user.birthDate?.let {
                LocalDate.ofEpochDay(it / 86_400_000L)
            }
        }
    }

    // ─── Events ─────────────────────────────────────────────────────────────

    fun onEvent(event: AddBodyCompositionEvent) {
        when (event) {
            is AddBodyCompositionEvent.UpdateHeight ->
                _uiState.update { it.copy(heightCm = event.value) }

            is AddBodyCompositionEvent.UpdateWeight ->
                _uiState.update { it.copy(weightKg = event.value) }

            is AddBodyCompositionEvent.UpdateBodyFat ->
                _uiState.update { it.copy(bodyFatPercent = event.value) }

            is AddBodyCompositionEvent.UpdateWater ->
                _uiState.update { it.copy(waterPercent = event.value) }

            is AddBodyCompositionEvent.UpdateMuscleMass ->
                _uiState.update { it.copy(muscleMassKg = event.value) }

            is AddBodyCompositionEvent.UpdateVisceralFat ->
                _uiState.update { it.copy(visceralFatPercent = event.value) }

            is AddBodyCompositionEvent.UpdateBasalCalories ->
                _uiState.update { it.copy(basalCalories = event.value) }

            is AddBodyCompositionEvent.UpdateMetabolicAge ->
                _uiState.update { it.copy(metabolicAge = event.value) }

            is AddBodyCompositionEvent.UpdateBoneMass ->
                _uiState.update { it.copy(boneMassKg = event.value) }

            is AddBodyCompositionEvent.UpdateChest ->
                _uiState.update { it.copy(chestCm = event.value) }

            is AddBodyCompositionEvent.UpdateWaist ->
                _uiState.update { it.copy(waistCm = event.value) }

            is AddBodyCompositionEvent.UpdateHip ->
                _uiState.update { it.copy(hipCm = event.value) }

            is AddBodyCompositionEvent.UpdateGlute ->
                _uiState.update { it.copy(gluteCm = event.value) }

            is AddBodyCompositionEvent.UpdateLeftArm ->
                _uiState.update { it.copy(leftArmCm = event.value) }

            is AddBodyCompositionEvent.UpdateRightArm ->
                _uiState.update { it.copy(rightArmCm = event.value) }

            is AddBodyCompositionEvent.UpdateLeftLeg ->
                _uiState.update { it.copy(leftLegCm = event.value) }

            is AddBodyCompositionEvent.UpdateRightLeg ->
                _uiState.update { it.copy(rightLegCm = event.value) }

            is AddBodyCompositionEvent.UpdateDate ->
                _uiState.update { it.copy(date = event.date) }

            AddBodyCompositionEvent.DismissError ->
                _uiState.update { it.copy(errorMessage = null) }

            AddBodyCompositionEvent.Save -> save()
        }
    }

    // ─── Save ────────────────────────────────────────────────────────────────

    private fun save() {
        val form = _uiState.value

        if (!form.isFormValid) {
            _uiState.update { it.copy(errorMessage = "Height and weight are required.") }
            return
        }

        _uiState.update { it.copy(isSaving = true, errorMessage = null) }

        viewModelScope.launch {
            val userId = cachedUserId
            val gender = cachedGender
            val birthDate = cachedBirthDate

            if (userId == null || gender == null || birthDate == null) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "Profile incomplete. Please update your profile first.",
                    )
                }
                return@launch
            }

            val age = java.time.Period.between(birthDate, form.date).years

            val rawInput = RawBodyCompositionInput(
                userId = userId,
                date = form.date,
                heightCm = form.heightCm.toFloat(),
                gender = gender,
                age = age,
                weightKg = form.weightKg.toFloat(),
                bodyFatPercent = form.bodyFatPercent.toFloatOrNull(),
                waterPercent = form.waterPercent.toFloatOrNull(),
                muscleMassKg = form.muscleMassKg.toFloatOrNull(),
                visceralFatPercent = form.visceralFatPercent.toFloatOrNull(),
                basalCalories = form.basalCalories.toIntOrNull(),
                metabolicAge = form.metabolicAge.toIntOrNull(),
                boneMassKg = form.boneMassKg.toFloatOrNull(),
                chestCm = form.chestCm.toFloatOrNull(),
                waistCm = form.waistCm.toFloatOrNull(),
                hipCm = form.hipCm.toFloatOrNull(),
                gluteCm = form.gluteCm.toFloatOrNull(),
                leftArmCm = form.leftArmCm.toFloatOrNull(),
                rightArmCm = form.rightArmCm.toFloatOrNull(),
                leftLegCm = form.leftLegCm.toFloatOrNull(),
                rightLegCm = form.rightLegCm.toFloatOrNull(),
            )

            saveBodyCompositionEntry(rawInput)
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false) }
                    _navigationEffect.emit(AddBodyCompositionNavEffect.SavedSuccessfully)
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = "Error saving: ${error.message}",
                        )
                    }
                }
        }
    }
}

/**
 * One-shot navigation effects emitted by [AddBodyCompositionViewModel].
 */
sealed interface AddBodyCompositionNavEffect {
    /** The entry was saved; the host should pop this screen. */
    data object SavedSuccessfully : AddBodyCompositionNavEffect
}