package com.example.todoappnew.data.local.database

import androidx.room.TypeConverter
import com.example.todoappnew.domain.model.TaskStatus

class TaskConverters {
    @TypeConverter
    fun fromStatus(status: TaskStatus): String = status.name

    @TypeConverter
    fun toStatus(status: String): TaskStatus = TaskStatus.valueOf(status)
}
