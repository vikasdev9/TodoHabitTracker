package com.example.todoappnew.domain.repository

import com.example.todoappnew.domain.model.Task
import com.example.todoappnew.domain.model.TaskStatus
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    fun getTasks(): Flow<List<Task>>
    fun searchTasks(query: String): Flow<List<Task>>
    fun filterTasks(status: TaskStatus): Flow<List<Task>>
    
    // Filter Preferences
    fun getDefaultFilter(): Flow<TaskStatus>
    suspend fun updateDefaultFilter(status: TaskStatus)
}
