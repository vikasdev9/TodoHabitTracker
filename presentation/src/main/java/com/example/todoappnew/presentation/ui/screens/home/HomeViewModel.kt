package com.example.todoappnew.presentation.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappnew.data.local.datastore.PreferencesDataSource
import com.example.todoappnew.domain.model.AppBackground
import com.example.todoappnew.domain.model.Task
import com.example.todoappnew.domain.model.TaskStatus
import com.example.todoappnew.domain.model.ViewType
import com.example.todoappnew.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val syncCompletedTasksUseCase: SyncCompletedTasksUseCase,
    private val preferencesDataSource: PreferencesDataSource,
    private val getBackgroundUseCase: GetBackgroundUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _activeFilter = MutableStateFlow<TaskStatus?>(null)
    private val _isSyncing = MutableStateFlow(false)
    private val _syncMessage = MutableStateFlow<String?>(null)

    private var recentlyDeletedTask: Task? = null

    @OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<HomeUiState> = combine(
        combine(
            _searchQuery.debounce(300L),
            _activeFilter,
            _isSyncing,
            _syncMessage,
            preferencesDataSource.viewType
        ) { query, filter, syncing, syncMsg, viewType ->
            PartialSnapshot(query, filter, syncing, syncMsg, viewType)
        },
        getBackgroundUseCase()
    ) { partial, background ->
        DataSnapshot(partial.query, partial.filter, partial.isSyncing, partial.syncMessage, partial.viewType, background)
    }.flatMapLatest { snapshot ->
        getTasksUseCase().map { tasks ->
            val filteredTasks = tasks
                .filter { task ->
                    val matchesQuery = task.title.contains(snapshot.query, ignoreCase = true) || 
                                       task.description.contains(snapshot.query, ignoreCase = true)
                    val matchesFilter = snapshot.filter == null || task.status == snapshot.filter
                    matchesQuery && matchesFilter
                }
                .sortedBy { it.scheduledTime }

            HomeUiState(
                tasks = filteredTasks,
                viewType = snapshot.viewType,
                background = snapshot.background,
                searchQuery = snapshot.query,
                activeFilter = snapshot.filter,
                isSyncing = snapshot.isSyncing,
                syncMessage = snapshot.syncMessage
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SearchQueryChanged -> _searchQuery.value = event.query
            is HomeEvent.FilterChanged -> _activeFilter.value = event.status
            is HomeEvent.DeleteTask -> {
                viewModelScope.launch {
                    recentlyDeletedTask = event.task
                    deleteTaskUseCase(event.task)
                }
            }
            is HomeEvent.ToggleTaskCompletion -> {
                viewModelScope.launch {
                    val newStatus = if (!event.task.isCompleted) TaskStatus.COMPLETED else TaskStatus.PENDING
                    updateTaskUseCase(event.task.copy(
                        isCompleted = !event.task.isCompleted,
                        status = newStatus
                    ))
                }
            }
            is HomeEvent.ChangeViewType -> {
                viewModelScope.launch {
                    preferencesDataSource.updateViewType(event.viewType)
                }
            }
            HomeEvent.UndoDelete -> {
                viewModelScope.launch {
                    recentlyDeletedTask?.let { addTaskUseCase(it) }
                    recentlyDeletedTask = null
                }
            }
            HomeEvent.SyncTasks -> syncTasks()
            HomeEvent.DismissSyncMessage -> _syncMessage.value = null
        }
    }

    private fun syncTasks() {
        viewModelScope.launch {
            _isSyncing.value = true
            val result = syncCompletedTasksUseCase()
            _isSyncing.value = false
            
            _syncMessage.value = if (result.isSuccess) {
                "Sync successful!"
            } else {
                "Sync failed: ${result.exceptionOrNull()?.message}"
            }
        }
    }

    private data class PartialSnapshot(
        val query: String,
        val filter: TaskStatus?,
        val isSyncing: Boolean,
        val syncMessage: String?,
        val viewType: ViewType
    )

    private data class DataSnapshot(
        val query: String,
        val filter: TaskStatus?,
        val isSyncing: Boolean,
        val syncMessage: String?,
        val viewType: ViewType,
        val background: AppBackground
    )
}
