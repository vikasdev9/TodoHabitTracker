package com.example.todoappnew.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.todoappnew.domain.model.AppTheme
import com.example.todoappnew.domain.model.AppBackground
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "theme_settings")

@Singleton
class ThemeDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val themeKey = stringPreferencesKey("theme_mode")
    private val backgroundKey = stringPreferencesKey("background_theme")

    val themeMode: Flow<AppTheme> = context.dataStore.data.map { preferences ->
        val themeName = preferences[themeKey] ?: AppTheme.SYSTEM.name
        try {
            AppTheme.valueOf(themeName)
        } catch (e: Exception) {
            AppTheme.SYSTEM
        }
    }

    val backgroundTheme: Flow<AppBackground> = context.dataStore.data.map { preferences ->
        val bgName = preferences[backgroundKey] ?: AppBackground.DEFAULT.name
        try {
            AppBackground.valueOf(bgName)
        } catch (e: Exception) {
            AppBackground.DEFAULT
        }
    }

    suspend fun updateThemeMode(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = theme.name
        }
    }

    suspend fun updateBackgroundTheme(background: AppBackground) {
        context.dataStore.edit { preferences ->
            preferences[backgroundKey] = background.name
        }
    }
}
