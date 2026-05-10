package com.example.todoappnew.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappnew.domain.model.AppBackground
import com.example.todoappnew.domain.model.AppTheme
import com.example.todoappnew.domain.usecase.GetBackgroundUseCase
import com.example.todoappnew.domain.usecase.GetThemeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getThemeUseCase: GetThemeUseCase,
    getBackgroundUseCase: GetBackgroundUseCase
) : ViewModel() {

    val theme: StateFlow<AppTheme> = getThemeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.SYSTEM
        )

    val background: StateFlow<AppBackground> = getBackgroundUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppBackground.DEFAULT
        )
}
