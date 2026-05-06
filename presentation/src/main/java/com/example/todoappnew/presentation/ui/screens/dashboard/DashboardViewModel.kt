package com.example.todoappnew.presentation.ui.screens.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappnew.domain.model.TimeFilter
import com.example.todoappnew.domain.usecase.ExportReportUseCase
import com.example.todoappnew.domain.usecase.GetAnalyticsUseCase
import com.example.todoappnew.domain.usecase.GetProgressTrendUseCase
import com.example.todoappnew.presentation.util.ShareUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAnalyticsUseCase: GetAnalyticsUseCase,
    private val getProgressTrendUseCase: GetProgressTrendUseCase,
    private val exportReportUseCase: ExportReportUseCase
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow(TimeFilter.TODAY)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<DashboardUiState> = _selectedFilter
        .flatMapLatest { filter ->
            combine(
                getAnalyticsUseCase(filter),
                getProgressTrendUseCase()
            ) { analytics, trend ->
                DashboardUiState(
                    analyticsData = analytics,
                    weeklyProgress = trend,
                    selectedFilter = filter,
                    isLoading = false
                )
            }.onStart {
                emit(DashboardUiState(selectedFilter = filter, isLoading = true))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DashboardUiState(isLoading = true)
        )

    fun changeFilter(filter: TimeFilter) {
        if (_selectedFilter.value != filter) {
            _selectedFilter.value = filter
        }
    }

    fun exportReport(context: Context) {
        val currentState = uiState.value
        if (currentState.isLoading) return

        val csvContent = exportReportUseCase(
            data = currentState.analyticsData,
            timeFilterName = currentState.selectedFilter.name
        )
        
        viewModelScope.launch {
            try {
                val fileName = "TodoReport_${currentState.selectedFilter.name}_${System.currentTimeMillis()}.csv"
                val file = File(context.cacheDir, fileName)
                file.writeText(csvContent)
                ShareUtils.shareFile(context, file, "Export Todo Report")
            } catch (e: Exception) {
                // Error handling
            }
        }
    }
}
