package com.example.todoappnew.presentation.ui.screens.task_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappnew.domain.model.Task
import com.example.todoappnew.domain.model.TaskStatus
import com.example.todoappnew.domain.usecase.AddTaskUseCase
import com.example.todoappnew.domain.usecase.UpdateTaskUseCase
import com.example.todoappnew.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    private var currentTaskId: String? = null

    init {
        savedStateHandle.get<String>("taskId")?.let { id ->
            if (id.isNotBlank()) {
                currentTaskId = id
                loadTask(id)
            }
        }
    }

    private fun loadTask(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getTasks().first().find { it.id == id }?.let { task ->
                _uiState.update {
                    it.copy(
                        title = task.title,
                        description = task.description,
                        scheduledTime = task.scheduledTime,
                        imageUri = task.imageUri,
                        videoUri = task.videoUri,
                        stickerId = task.stickerId,
                        isCompleted = task.isCompleted,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onEvent(event: TaskDetailEvent) {
        when (event) {
            is TaskDetailEvent.TitleChanged -> _uiState.update { it.copy(title = event.title) }
            is TaskDetailEvent.DescriptionChanged -> _uiState.update { it.copy(description = event.description) }
            is TaskDetailEvent.DateTimeChanged -> _uiState.update { it.copy(scheduledTime = event.millis) }
            is TaskDetailEvent.ImageSelected -> _uiState.update { it.copy(imageUri = event.uri) }
            is TaskDetailEvent.VideoSelected -> _uiState.update { it.copy(videoUri = event.uri) }
            is TaskDetailEvent.StickerSelected -> _uiState.update { it.copy(stickerId = event.stickerId) }
            TaskDetailEvent.SaveTask -> saveTask()
        }
    }

    private fun saveTask() {
        val state = _uiState.value
        if (state.title.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Title cannot be empty") }
            return
        }

        viewModelScope.launch {
            val status = if (state.isCompleted) {
                TaskStatus.COMPLETED
            } else if (state.scheduledTime < System.currentTimeMillis()) {
                TaskStatus.MISSED
            } else {
                TaskStatus.PENDING
            }

            val task = Task(
                id = currentTaskId ?: java.util.UUID.randomUUID().toString(),
                title = state.title,
                description = state.description,
                scheduledTime = state.scheduledTime,
                imageUri = state.imageUri,
                videoUri = state.videoUri,
                stickerId = state.stickerId,
                isCompleted = state.isCompleted,
                status = status,
                createdAt = System.currentTimeMillis()
            )

            if (currentTaskId == null) {
                addTaskUseCase(task)
            } else {
                updateTaskUseCase(task)
            }
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}
