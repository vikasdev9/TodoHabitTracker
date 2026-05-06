package com.example.todoappnew.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoappnew.domain.model.TaskStatus

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val isSynced: Boolean = false,
    val status: TaskStatus,
    val imageUri: String?,
    val videoUri: String?,
    val stickerId: String?,
    val scheduledTime: Long,
    val createdAt: Long
)
