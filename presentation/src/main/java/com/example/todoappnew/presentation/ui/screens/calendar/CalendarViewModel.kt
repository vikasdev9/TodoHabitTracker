package com.example.todoappnew.presentation.ui.screens.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappnew.domain.model.Task
import com.example.todoappnew.domain.model.TaskStatus
import com.example.todoappnew.domain.usecase.GetTasksUseCase
import com.example.todoappnew.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.Instant
import javax.inject.Inject

data class CalendarUiState(
    val selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val tasks: List<Task> = emptyList(),
    val allTasks: List<Task> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(Clock.System.todayIn(TimeZone.currentSystemDefault()))
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    val uiState: StateFlow<CalendarUiState> = combine(
        _selectedDate,
        getTasksUseCase()
    ) { date, allTasks ->
        val filteredTasks = allTasks.filter { task ->
            val taskDate = Instant.fromEpochMilliseconds(task.scheduledTime)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
            taskDate == date
        }
        CalendarUiState(
            selectedDate = date,
            tasks = filteredTasks.sortedBy { it.scheduledTime },
            allTasks = allTasks,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CalendarUiState()
    )

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val isCurrentlyCompleted = task.status == TaskStatus.COMPLETED
            val updatedTask = task.copy(
                isCompleted = !isCurrentlyCompleted,
                status = if (!isCurrentlyCompleted) TaskStatus.COMPLETED else TaskStatus.PENDING
            )
            updateTaskUseCase(updatedTask)
        }
    }
}
