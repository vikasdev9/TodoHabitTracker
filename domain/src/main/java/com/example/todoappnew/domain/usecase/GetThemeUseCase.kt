package com.example.todoappnew.domain.usecase

import com.example.todoappnew.domain.model.AppTheme
import com.example.todoappnew.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    operator fun invoke(): Flow<AppTheme> = repository.getTheme()
}
