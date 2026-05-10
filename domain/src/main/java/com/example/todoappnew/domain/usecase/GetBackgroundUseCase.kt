package com.example.todoappnew.domain.usecase

import com.example.todoappnew.domain.model.AppBackground
import com.example.todoappnew.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBackgroundUseCase @Inject constructor(
    private val repository: ThemeRepository
) {
    operator fun invoke(): Flow<AppBackground> = repository.getBackground()
}
