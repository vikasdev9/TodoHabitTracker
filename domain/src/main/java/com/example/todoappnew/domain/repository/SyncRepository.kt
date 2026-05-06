package com.example.todoappnew.domain.repository

interface SyncRepository {
    suspend fun syncCompletedTasks(): Result<Unit>
}
