package com.example.todoappnew.data.mapper

import com.example.todoappnew.data.local.entity.TaskEntity
import com.example.todoappnew.domain.model.Task

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        isSynced = isSynced,
        status = status,
        imageUri = imageUri,
        videoUri = videoUri,
        stickerId = stickerId,
        scheduledTime = scheduledTime,
        createdAt = createdAt
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        isSynced = isSynced,
        status = status,
        imageUri = imageUri,
        videoUri = videoUri,
        stickerId = stickerId,
        scheduledTime = scheduledTime,
        createdAt = createdAt
    )
}
