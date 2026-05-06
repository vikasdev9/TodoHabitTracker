package com.example.todoappnew.domain.usecase

import com.example.todoappnew.domain.repository.SyncRepository
import javax.inject.Inject

class SyncCompletedTasksUseCase @Inject constructor(
    private val repository: SyncRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.syncCompletedTasks()
}
