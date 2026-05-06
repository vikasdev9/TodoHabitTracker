package com.example.todoappnew.domain.repository

import com.example.todoappnew.domain.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getTheme(): Flow<AppTheme>
    suspend fun setTheme(theme: AppTheme)
}
