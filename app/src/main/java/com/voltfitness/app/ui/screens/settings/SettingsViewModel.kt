package com.voltfitness.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.voltfitness.app.data.repository.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeRepository: ThemeRepository
) : ViewModel() {
    val isDarkMode = themeRepository.isDarkMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            themeRepository.setDarkMode(isDark)
        }
    }
}

@HiltViewModel
class MainViewModel @Inject constructor(
    themeRepository: ThemeRepository
) : ViewModel() {
    val isDarkMode = themeRepository.isDarkMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = true
    )
}