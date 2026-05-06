package com.example.todoappnew.presentation.ui.screens.home

import com.example.todoappnew.domain.model.Task
import com.example.todoappnew.domain.model.TaskStatus
import com.example.todoappnew.domain.model.ViewType

data class HomeUiState(
    val tasks: List<Task> = emptyList(),
    val viewType: ViewType = ViewType.LIST,
    val isLoading: Boolean = false,
    val isSyncing: Boolean = false,
    val syncMessage: String? = null,
    val searchQuery: String = "",
    val activeFilter: TaskStatus? = null,
    val errorMessage: String? = null
)

sealed class HomeEvent {
    data class SearchQueryChanged(val query: String) : HomeEvent()
    data class FilterChanged(val status: TaskStatus?) : HomeEvent()
    data class DeleteTask(val task: Task) : HomeEvent()
    data class ToggleTaskCompletion(val task: Task) : HomeEvent()
    data class ChangeViewType(val viewType: ViewType) : HomeEvent()
    object UndoDelete : HomeEvent()
    object SyncTasks : HomeEvent()
    object DismissSyncMessage : HomeEvent()
}
