package com.voltfitness.app.ui.screens.body_composition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voltfitness.app.domain.model.BodyCompositionEntry
import com.voltfitness.app.domain.model.User
import com.voltfitness.app.domain.repository.BodyCompositionRepository
import com.voltfitness.app.domain.repository.UserRepository
import com.voltfitness.app.domain.usecase.body_composition.RawBodyCompositionInput
import com.voltfitness.app.domain.usecase.body_composition.SaveBodyCompositionEntryUseCase
import com.voltfitness.app.ui.screens.body_composition.add.AddBodyCompositionEvent
import com.voltfitness.app.ui.screens.body_composition.add.AddBodyCompositionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class BodyCompositionViewModel @Inject constructor(
    private val saveBodyCompositionEntry: SaveBodyCompositionEntryUseCase,
    private val bodyCompositionRepository: BodyCompositionRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    // ─── State Holders ───
    private val _uiState = MutableStateFlow(BodyCompositionUiState())
    val uiState: StateFlow<BodyCompositionUiState> = _uiState.asStateFlow()

    private val _addUiState = MutableStateFlow(AddBodyCompositionUiState())
    val addUiState: StateFlow<AddBodyCompositionUiState> = _addUiState.asStateFlow()

    private var cachedUser: User? = null

    init {
        loadUserData()
    }

    // ─── Main Screen ───

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val userId = userRepository.getCurrentUserId()
                if (userId == null) {
                    _uiState.update { it.copy(isLoading = false) }
                    return@launch
                }

                val user = userRepository.getUserById(userId)
                cachedUser = user

                bodyCompositionRepository
                    .getRecentEntries(userId, limit = 50)
                    .collect { entries ->
                        val displayItems = entries.map { it.toDisplayItem() }
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                userName = user?.name ?: "User",
                                gender = user?.gender,
                                goal = user?.goal,
                                latestEntry = displayItems.firstOrNull(),
                                historyEntries = displayItems
                            )
                        }
                    }
            } catch (_: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // ─── Add Form: Pre-load ───

    /**
     * Resets the add form to a clean state.
     * No profile data is loaded — this form only handles body metrics.
     */
    fun resetAddForm() {
        viewModelScope.launch {
            if (cachedUser == null) {
                val userId = userRepository.getCurrentUserId()
                if (userId != null) cachedUser = userRepository.getUserById(userId)
            }

            _addUiState.value = AddBodyCompositionUiState(
                date = LocalDate.now()
            )
        }
    }

    // ─── Add Form: Events ───

    fun onAddEvent(event: AddBodyCompositionEvent) {
        when (event) {
            // Body Composition
            is AddBodyCompositionEvent.UpdateHeight ->
                _addUiState.update { it.copy(heightCm = event.value) }
            is AddBodyCompositionEvent.UpdateWeight ->
                _addUiState.update { it.copy(weightKg = event.value) }
            is AddBodyCompositionEvent.UpdateBodyFat ->
                _addUiState.update { it.copy(bodyFatPercent = event.value) }
            is AddBodyCompositionEvent.UpdateWater ->
                _addUiState.update { it.copy(waterPercent = event.value) }
            is AddBodyCompositionEvent.UpdateMuscleMass ->
                _addUiState.update { it.copy(muscleMassKg = event.value) }
            is AddBodyCompositionEvent.UpdateVisceralFat ->
                _addUiState.update { it.copy(visceralFatPercent = event.value) }
            is AddBodyCompositionEvent.UpdateBasalCalories ->
                _addUiState.update { it.copy(basalCalories = event.value) }
            is AddBodyCompositionEvent.UpdateMetabolicAge ->
                _addUiState.update { it.copy(metabolicAge = event.value) }
            is AddBodyCompositionEvent.UpdateBoneMass ->
                _addUiState.update { it.copy(boneMassKg = event.value) }

            // Body Measurements
            is AddBodyCompositionEvent.UpdateChest ->
                _addUiState.update { it.copy(chestCm = event.value) }
            is AddBodyCompositionEvent.UpdateWaist ->
                _addUiState.update { it.copy(waistCm = event.value) }
            is AddBodyCompositionEvent.UpdateHip ->
                _addUiState.update { it.copy(hipCm = event.value) }
            is AddBodyCompositionEvent.UpdateGlute ->
                _addUiState.update { it.copy(gluteCm = event.value) }
            is AddBodyCompositionEvent.UpdateLeftArm ->
                _addUiState.update { it.copy(leftArmCm = event.value) }
            is AddBodyCompositionEvent.UpdateRightArm ->
                _addUiState.update { it.copy(rightArmCm = event.value) }
            is AddBodyCompositionEvent.UpdateLeftLeg ->
                _addUiState.update { it.copy(leftLegCm = event.value) }
            is AddBodyCompositionEvent.UpdateRightLeg ->
                _addUiState.update { it.copy(rightLegCm = event.value) }

            // General
            is AddBodyCompositionEvent.UpdateDate ->
                _addUiState.update { it.copy(date = event.date) }
            AddBodyCompositionEvent.DismissError ->
                _addUiState.update { it.copy(errorMessage = null) }
            AddBodyCompositionEvent.Save -> saveMeasurement()
        }
    }

    // ─── Save ───
    private fun saveMeasurement() {
        val form = _addUiState.value

        if (!form.isFormValid) {
            _addUiState.update {
                it.copy(errorMessage = "Height and weight are required.")
            }
            return
        }

        viewModelScope.launch {
            _addUiState.update { it.copy(isSaving = true, errorMessage = null) }

            try {
                val user = cachedUser ?: run {
                    val id = userRepository.getCurrentUserId()
                        ?: throw IllegalStateException("No user found.")
                    userRepository.getUserById(id)
                        ?: throw IllegalStateException("No user found.")
                }

                val gender = user.gender
                    ?: throw IllegalStateException("Gender is required. Update your profile first.")

                val birthDate = user.birthDate?.let {
                    LocalDate.ofEpochDay(it / 86_400_000L)
                } ?: throw IllegalStateException("Birth date is required. Update your profile first.")

                val age = java.time.Period.between(birthDate, form.date).years

                val rawInput = RawBodyCompositionInput(
                    userId = user.id,
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
                    rightLegCm = form.rightLegCm.toFloatOrNull()
                )

                saveBodyCompositionEntry(rawInput)

                _addUiState.update { it.copy(isSaving = false, saveSuccess = true) }
                loadUserData()
            } catch (e: Exception) {
                _addUiState.update {
                    it.copy(isSaving = false, errorMessage = "Error saving: ${e.message}")
                }
            }
        }
    }

    // ─── Mapping ───

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
        compositionScore = compositionScore
    )
}
