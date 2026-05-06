package com.example.todoappnew.presentation.ui.screens.task_detail

data class TaskDetailUiState(
    val title: String = "",
    val description: String = "",
    val scheduledTime: Long = System.currentTimeMillis(),
    val imageUri: String? = null,
    val videoUri: String? = null,
    val stickerId: String? = null,
    val isCompleted: Boolean = false,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)

sealed class TaskDetailEvent {
    data class TitleChanged(val title: String) : TaskDetailEvent()
    data class DescriptionChanged(val description: String) : TaskDetailEvent()
    data class DateTimeChanged(val millis: Long) : TaskDetailEvent()
    data class ImageSelected(val uri: String?) : TaskDetailEvent()
    data class VideoSelected(val uri: String?) : TaskDetailEvent()
    data class StickerSelected(val stickerId: String?) : TaskDetailEvent()
    object SaveTask : TaskDetailEvent()
}
