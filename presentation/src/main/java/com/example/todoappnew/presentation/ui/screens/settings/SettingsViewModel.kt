package com.example.todoappnew.presentation.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappnew.domain.model.AppTheme
import com.example.todoappnew.domain.model.AppBackground
import com.example.todoappnew.domain.model.TaskStatus
import com.example.todoappnew.domain.repository.TaskRepository
import com.example.todoappnew.domain.usecase.GetThemeUseCase
import com.example.todoappnew.domain.usecase.SetThemeUseCase
import com.example.todoappnew.domain.usecase.GetBackgroundUseCase
import com.example.todoappnew.domain.usecase.SetBackgroundUseCase
import com.example.todoappnew.presentation.PreviewStateManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * SettingsViewModel manages the application's configuration state.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val getThemeUseCase: GetThemeUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    private val getBackgroundUseCase: GetBackgroundUseCase,
    private val setBackgroundUseCase: SetBackgroundUseCase,
    private val previewStateManager: PreviewStateManager
) : ViewModel() {

    // Event channel to trigger platform-specific actions (like icon switching)
    private val _themeChangedEvent = MutableSharedFlow<AppTheme>()
    val themeChangedEvent = _themeChangedEvent.asSharedFlow()

    private val _previewBackground = MutableStateFlow<AppBackground?>(null)

    val uiState: StateFlow<SettingsUiState> = combine(
        getThemeUseCase(),
        getBackgroundUseCase(),
        repository.getDefaultFilter(),
        _previewBackground
    ) { theme, background, filter, preview ->
        previewStateManager.setPreviewActive(preview != null)
        SettingsUiState(
            theme = theme,
            background = background,
            selectedPreviewBackground = preview,
            defaultFilter = filter.name
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ThemeChanged -> updateTheme(event.theme)
            is SettingsEvent.BackgroundChanged -> {
                updateBackground(event.background)
                _previewBackground.value = null
            }
            is SettingsEvent.ShowPreview -> {
                _previewBackground.value = event.background
            }
            is SettingsEvent.DismissPreview -> {
                _previewBackground.value = null
            }
            is SettingsEvent.DefaultFilterChanged -> updateDefaultFilter(event.filter)
        }
    }

    private fun updateTheme(theme: AppTheme) {
        viewModelScope.launch {
            setThemeUseCase(theme)
            _themeChangedEvent.emit(theme)
        }
    }

    private fun updateBackground(background: AppBackground) {
        viewModelScope.launch {
            setBackgroundUseCase(background)
        }
    }

    private fun updateDefaultFilter(filter: String) {
        viewModelScope.launch {
            try {
                repository.updateDefaultFilter(TaskStatus.valueOf(filter))
            } catch (e: IllegalArgumentException) {
                // Handle invalid filter
            }
        }
    }
}
