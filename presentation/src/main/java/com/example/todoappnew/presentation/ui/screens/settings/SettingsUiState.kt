package com.example.todoappnew.presentation.ui.screens.settings

import com.example.todoappnew.domain.model.AppTheme

data class SettingsUiState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val defaultFilter: String = "PENDING"
)

sealed class SettingsEvent {
    data class ThemeChanged(val theme: AppTheme) : SettingsEvent()
    data class DefaultFilterChanged(val filter: String) : SettingsEvent()
}
