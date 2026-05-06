package com.example.todoappnew.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoappnew.data.local.dao.TaskDao
import com.example.todoappnew.data.local.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
@TypeConverters(TaskConverters::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
}
