package com.voltfitness.app.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voltfitness.app.domain.usecase.user.CheckUserExistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SessionState {
    data object Loading : SessionState
    data object Authenticated : SessionState
    data object Unauthenticated : SessionState
}

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val checkUserExistsUseCase: CheckUserExistsUseCase
) : ViewModel() {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            try {
                val userExists = checkUserExistsUseCase()
                _sessionState.value = if (userExists) {
                    SessionState.Authenticated
                } else {
                    SessionState.Unauthenticated
                }
            } catch (e: Exception) {
                _sessionState.value = SessionState.Unauthenticated
            }
        }
    }

    fun onUserRegistered() {
        _sessionState.value = SessionState.Authenticated
    }
}
