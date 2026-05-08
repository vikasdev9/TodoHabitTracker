package com.example.todoappnew.domain.usecase

import com.example.todoappnew.domain.model.AppBackground
import com.example.todoappnew.domain.repository.ThemeRepository
import javax.inject.Inject

class SetBackgroundUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    suspend operator fun invoke(background: AppBackground) = repository.setBackground(background)
}
