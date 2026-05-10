package com.example.todoappnew.presentation.ui.screens.settings

import com.example.todoappnew.domain.model.AppTheme
import com.example.todoappnew.domain.model.AppBackground

data class SettingsUiState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val background: AppBackground = AppBackground.DEFAULT,
    val selectedPreviewBackground: AppBackground? = null,
    val defaultFilter: String = "PENDING"
)

sealed class SettingsEvent {
    data class ThemeChanged(val theme: AppTheme) : SettingsEvent()
    data class BackgroundChanged(val background: AppBackground) : SettingsEvent()
    data class ShowPreview(val background: AppBackground) : SettingsEvent()
    object DismissPreview : SettingsEvent()
    data class DefaultFilterChanged(val filter: String) : SettingsEvent()
}
