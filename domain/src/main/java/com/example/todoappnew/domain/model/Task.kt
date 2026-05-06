package com.example.todoappnew.domain.model

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val isSynced: Boolean = false,
    val status: TaskStatus = TaskStatus.PENDING,
    val imageUri: String? = null,
    val videoUri: String? = null,
    val stickerId: String? = null,
    val scheduledTime: Long,
    val createdAt: Long = System.currentTimeMillis()
)
