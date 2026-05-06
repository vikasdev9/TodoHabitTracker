package com.example.todoappnew.domain.usecase

import com.example.todoappnew.domain.model.Task
import com.example.todoappnew.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> = repository.getTasks()
}
