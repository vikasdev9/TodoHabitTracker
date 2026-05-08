package com.example.todoappnew.data.repository

import com.example.todoappnew.data.local.datastore.ThemeDataSource
import com.example.todoappnew.domain.model.AppTheme
import com.example.todoappnew.domain.model.AppBackground
import com.example.todoappnew.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val themeDataSource: ThemeDataSource
) : ThemeRepository {

    override fun getTheme(): Flow<AppTheme> {
        return themeDataSource.themeMode
    }

    override suspend fun setTheme(theme: AppTheme) {
        themeDataSource.updateThemeMode(theme)
    }

    override fun getBackground(): Flow<AppBackground> {
        return themeDataSource.backgroundTheme
    }

    override suspend fun setBackground(background: AppBackground) {
        themeDataSource.updateBackgroundTheme(background)
    }
}
