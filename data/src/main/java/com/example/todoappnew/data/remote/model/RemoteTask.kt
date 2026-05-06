package com.example.todoappnew.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteTask(
    @SerialName("id")
    val id: String? = null,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("is_completed")
    val isCompleted: Boolean,
    @SerialName("scheduled_time")
    val scheduledTime: Long,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("video_url")
    val videoUrl: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)
