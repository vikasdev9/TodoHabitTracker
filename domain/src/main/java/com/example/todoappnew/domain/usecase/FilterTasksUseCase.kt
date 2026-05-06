package com.example.todoappnew.domain.usecase

import com.example.todoappnew.domain.model.Task
import com.example.todoappnew.domain.model.TaskStatus
import com.example.todoappnew.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FilterTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(status: TaskStatus): Flow<List<Task>> = 
        repository.filterTasks(status)
}
