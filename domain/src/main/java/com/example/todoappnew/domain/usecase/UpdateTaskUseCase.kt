package com.example.todoappnew.domain.usecase

import com.example.todoappnew.domain.model.Task
import com.example.todoappnew.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) = repository.updateTask(task)
}
