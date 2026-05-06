package com.example.todoappnew.data.di

import android.content.Context
import androidx.room.Room
import com.example.todoappnew.data.local.dao.TaskDao
import com.example.todoappnew.data.local.database.TodoDatabase
import com.example.todoappnew.data.repository.SyncRepositoryImpl
import com.example.todoappnew.data.repository.TaskRepositoryImpl
import com.example.todoappnew.data.repository.ThemeRepositoryImpl
import com.example.todoappnew.domain.repository.SyncRepository
import com.example.todoappnew.domain.repository.TaskRepository
import com.example.todoappnew.domain.repository.ThemeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindThemeRepository(
        themeRepositoryImpl: ThemeRepositoryImpl
    ): ThemeRepository

    @Binds
    @Singleton
    abstract fun bindSyncRepository(
        syncRepositoryImpl: SyncRepositoryImpl
    ): SyncRepository

    companion object {
        @Provides
        @Singleton
        fun provideTodoDatabase(@ApplicationContext context: Context): TodoDatabase {
            return Room.databaseBuilder(
                context,
                TodoDatabase::class.java,
                "todo_db"
            ).build()
        }

        @Provides
        fun provideTaskDao(database: TodoDatabase): TaskDao {
            return database.taskDao
        }
    }
}
