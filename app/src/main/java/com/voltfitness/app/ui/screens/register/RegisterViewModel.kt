package com.voltfitness.app.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voltfitness.app.domain.model.User
import com.voltfitness.app.domain.usecase.home.EnsureDefaultFolderUseCase
import com.voltfitness.app.domain.usecase.user.CompleteUserRegistrationUseCase
import com.voltfitness.app.domain.usecase.user.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val gender: String = "",
    val birthDate: LocalDate? = null,
    val activityLevel: String = "",
    val goal: String = "",
    val experienceLevel: String = "",
    val gymName: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentStep: Int = 0 // For multi-step registration
) {
    val isNameValid: Boolean get() = name.isNotBlank()
    val isGenderValid: Boolean get() = gender.isNotBlank()
    val isActivityValid: Boolean get() = activityLevel.isNotBlank()
    val isGoalValid: Boolean get() = goal.isNotBlank()
    val isExperienceLvlValid: Boolean get() = experienceLevel.isNotBlank()
    val isBirthDateValid: Boolean get() = birthDate != null
    val canProceed: Boolean get() = isNameValid && isGenderValid && isActivityValid && isGoalValid && isExperienceLvlValid && isBirthDateValid
}

sealed interface RegisterEvent {
    data class UpdateName(val value: String) : RegisterEvent
    data class UpdateEmail(val value: String) : RegisterEvent
    data class UpdateGender(val value: String) : RegisterEvent
    data class UpdateBirthDate(val date: LocalDate?) : RegisterEvent
    data class UpdateActivityLevel(val value: String) : RegisterEvent
    data class UpdateGoal(val value: String) : RegisterEvent
    data class UpdateExperienceLevel(val value: String) : RegisterEvent
    data class UpdateGymName(val value: String) : RegisterEvent
    data class SetStep(val step: Int) : RegisterEvent
    data object NextStep : RegisterEvent
    data object PreviousStep : RegisterEvent
    data object Submit : RegisterEvent
    data object DismissError : RegisterEvent
}

sealed interface RegisterNavigationEffect {
    data object RegistrationComplete : RegisterNavigationEffect
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val completeUserRegistrationUseCase: CompleteUserRegistrationUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _navigationEffect = Channel<RegisterNavigationEffect>()
    val navigationEffect = _navigationEffect.receiveAsFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.UpdateName -> _uiState.update { it.copy(name = event.value) }
            is RegisterEvent.UpdateEmail -> _uiState.update { it.copy(email = event.value) }
            is RegisterEvent.UpdateGender -> _uiState.update { it.copy(gender = event.value) }
            is RegisterEvent.UpdateBirthDate -> _uiState.update { it.copy(birthDate = event.date) }
            is RegisterEvent.UpdateActivityLevel -> _uiState.update { it.copy(activityLevel = event.value) }
            is RegisterEvent.UpdateGoal -> _uiState.update { it.copy(goal = event.value) }
            is RegisterEvent.UpdateExperienceLevel -> _uiState.update { it.copy(experienceLevel = event.value) }
            is RegisterEvent.UpdateGymName -> _uiState.update { it.copy(gymName = event.value) }
            is RegisterEvent.SetStep -> _uiState.update { it.copy(currentStep = event.step) }
            is RegisterEvent.NextStep -> _uiState.update { it.copy(currentStep = it.currentStep + 1) }
            is RegisterEvent.PreviousStep -> _uiState.update {
                it.copy(currentStep = (it.currentStep - 1).coerceAtLeast(0))
            }

            is RegisterEvent.DismissError -> _uiState.update { it.copy(errorMessage = null) }
            is RegisterEvent.Submit -> submitRegistration()
        }
    }

    private fun submitRegistration() {
        val state = _uiState.value
        if (!state.canProceed) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = completeUserRegistrationUseCase(
                User(
                    id = 0L,
                    name = state.name.trim(),
                    email = state.email.trim().ifBlank { null },
                    gender = state.gender.lowercase(),
                    birthDate = state.birthDate?.toEpochDay()?.times(86_400_000L),
                    activityLevel = state.activityLevel.lowercase(),
                    goal = state.goal,
                    experienceLevel = state.experienceLevel.lowercase(),
                    gymName = state.gymName.trim().ifBlank { null },
                    createdAt = System.currentTimeMillis(),
                    updatedAt = null
                )
            )

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                    _navigationEffect.send(RegisterNavigationEffect.RegistrationComplete)
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Registration failed"
                        )
                    }
                }
            )
        }
    }
}
