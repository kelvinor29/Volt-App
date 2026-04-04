package com.voltfitness.app.ui.screens.bodycomposition.add

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
import java.time.Period
import javax.inject.Inject

@HiltViewModel
class AddBodyCompositionViewModel @Inject constructor(
    private val saveBodyCompositionEntry: SaveBodyCompositionEntryUseCase,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBodyCompositionUiState())
    val uiState: StateFlow<AddBodyCompositionUiState> = _uiState.asStateFlow()

    private val _navigationEffect = MutableSharedFlow<AddBodyCompositionNavEffect>()
    val navigationEffect: SharedFlow<AddBodyCompositionNavEffect> = _navigationEffect.asSharedFlow()

    private var cachedUserId: Long? = null
    private var cachedGender: String? = null
    private var cachedBirthDate: LocalDate? = null

    init {
        prefetchUserData()
    }

    fun onEvent(event: AddBodyCompositionEvent) {
        when (event) {
            is AddBodyCompositionEvent.UpdateHeight ->
                updateState { copy(heightCm = event.value) }

            is AddBodyCompositionEvent.UpdateWeight ->
                updateState { copy(weightKg = event.value) }

            is AddBodyCompositionEvent.UpdateBodyFat ->
                updateState { copy(bodyFatPercent = event.value) }

            is AddBodyCompositionEvent.UpdateWater ->
                updateState { copy(waterPercent = event.value) }

            is AddBodyCompositionEvent.UpdateMuscleMass ->
                updateState { copy(muscleMassKg = event.value) }

            is AddBodyCompositionEvent.UpdateVisceralFat ->
                updateState { copy(visceralFatPercent = event.value) }

            is AddBodyCompositionEvent.UpdateBasalCalories ->
                updateState { copy(basalCalories = event.value) }

            is AddBodyCompositionEvent.UpdateMetabolicAge ->
                updateState { copy(metabolicAge = event.value) }

            is AddBodyCompositionEvent.UpdateBoneMass ->
                updateState { copy(boneMassKg = event.value) }

            is AddBodyCompositionEvent.UpdateChest ->
                updateState { copy(chestCm = event.value) }

            is AddBodyCompositionEvent.UpdateWaist ->
                updateState { copy(waistCm = event.value) }

            is AddBodyCompositionEvent.UpdateHip ->
                updateState { copy(hipCm = event.value) }

            is AddBodyCompositionEvent.UpdateGlute ->
                updateState { copy(gluteCm = event.value) }

            is AddBodyCompositionEvent.UpdateLeftArm ->
                updateState { copy(leftArmCm = event.value) }

            is AddBodyCompositionEvent.UpdateRightArm ->
                updateState { copy(rightArmCm = event.value) }

            is AddBodyCompositionEvent.UpdateLeftLeg ->
                updateState { copy(leftLegCm = event.value) }

            is AddBodyCompositionEvent.UpdateRightLeg ->
                updateState { copy(rightLegCm = event.value) }

            is AddBodyCompositionEvent.UpdateDate ->
                updateState { copy(date = event.date) }

            AddBodyCompositionEvent.DismissError ->
                updateState { copy(errorMessage = null) }

            AddBodyCompositionEvent.Save -> save()
        }
    }

    private fun prefetchUserData() {
        viewModelScope.launch {
            val userId = userRepository.getCurrentUserId() ?: return@launch
            val user = userRepository.getUserById(userId) ?: return@launch

            cachedUserId = user.id
            cachedGender = user.gender
            cachedBirthDate = user.birthDate?.let { millis ->
                LocalDate.ofEpochDay(millis / MILLIS_PER_DAY)
            }
        }
    }

    private fun save() {
        val form = uiState.value

        if (!form.isFormValid) {
            showError("Height and weight are required.")
            return
        }

        updateState { copy(isSaving = true, errorMessage = null) }

        viewModelScope.launch {
            val profileData = getCachedProfileData()
            if (profileData == null) {
                updateState {
                    copy(
                        isSaving = false,
                        errorMessage = "Profile incomplete. Please update your profile first."
                    )
                }
                return@launch
            }

            val rawInput = buildRawInput(
                form = form,
                userId = profileData.userId,
                gender = profileData.gender,
                birthDate = profileData.birthDate
            )

            saveBodyCompositionEntry(rawInput)
                .onSuccess {
                    updateState { copy(isSaving = false) }
                    _navigationEffect.emit(AddBodyCompositionNavEffect.SavedSuccessfully)
                }
                .onFailure { error ->
                    updateState {
                        copy(
                            isSaving = false,
                            errorMessage = "Error saving: ${error.message}"
                        )
                    }
                }
        }
    }

    private fun buildRawInput(
        form: AddBodyCompositionUiState,
        userId: Long,
        gender: String,
        birthDate: LocalDate,
    ): RawBodyCompositionInput {
        val age = Period.between(birthDate, form.date).years

        return RawBodyCompositionInput(
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
    }

    private fun getCachedProfileData(): CachedProfileData? {
        val userId = cachedUserId ?: return null
        val gender = cachedGender ?: return null
        val birthDate = cachedBirthDate ?: return null

        return CachedProfileData(
            userId = userId,
            gender = gender,
            birthDate = birthDate
        )
    }

    private fun updateState(transform: AddBodyCompositionUiState.() -> AddBodyCompositionUiState) {
        _uiState.update(transform)
    }

    private fun showError(message: String) {
        updateState { copy(errorMessage = message) }
    }

    private data class CachedProfileData(
        val userId: Long,
        val gender: String,
        val birthDate: LocalDate,
    )

    private companion object {
        const val MILLIS_PER_DAY = 86_400_000L
    }
}

/**
 * One-shot navigation effects emitted by [AddBodyCompositionViewModel].
 */
sealed interface AddBodyCompositionNavEffect {
    data object SavedSuccessfully : AddBodyCompositionNavEffect
}