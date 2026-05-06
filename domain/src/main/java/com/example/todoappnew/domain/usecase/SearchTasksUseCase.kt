package com.example.todoappnew.domain.usecase

import com.example.todoappnew.domain.model.Task
import com.example.todoappnew.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(query: String): Flow<List<Task>> {
        if (query.isBlank()) return repository.getTasks()
        return repository.searchTasks(query)
    }
}
