package com.example.todoappnew.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.todoappnew.domain.model.TaskStatus
import com.example.todoappnew.domain.model.ViewType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class PreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val themeKey = stringPreferencesKey("theme")
    private val defaultFilterKey = stringPreferencesKey("default_filter")
    private val viewTypeKey = stringPreferencesKey("view_type")

    val theme: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[themeKey] ?: "SYSTEM"
    }

    val defaultFilter: Flow<TaskStatus> = context.dataStore.data.map { preferences ->
        TaskStatus.valueOf(preferences[defaultFilterKey] ?: TaskStatus.PENDING.name)
    }

    val viewType: Flow<ViewType> = context.dataStore.data.map { preferences ->
        try {
            ViewType.valueOf(preferences[viewTypeKey] ?: ViewType.LIST.name)
        } catch (e: IllegalArgumentException) {
            ViewType.LIST
        }
    }

    suspend fun updateTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = theme
        }
    }

    suspend fun updateDefaultFilter(status: TaskStatus) {
        context.dataStore.edit { preferences ->
            preferences[defaultFilterKey] = status.name
        }
    }

    suspend fun updateViewType(viewType: ViewType) {
        context.dataStore.edit { preferences ->
            preferences[viewTypeKey] = viewType.name
        }
    }
}
