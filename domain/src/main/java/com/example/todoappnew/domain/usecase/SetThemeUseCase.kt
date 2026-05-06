package com.example.todoappnew.domain.usecase

import com.example.todoappnew.domain.model.AppTheme
import com.example.todoappnew.domain.repository.ThemeRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    suspend operator fun invoke(theme: AppTheme) = repository.setTheme(theme)
}
