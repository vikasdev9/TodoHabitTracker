package com.example.todoappnew.domain.repository

import com.example.todoappnew.domain.model.AppTheme
import com.example.todoappnew.domain.model.AppBackground
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getTheme(): Flow<AppTheme>
    suspend fun setTheme(theme: AppTheme)
    
    fun getBackground(): Flow<AppBackground>
    suspend fun setBackground(background: AppBackground)
}
