package com.example.todoappnew.presentation.ui.screens.dashboard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoappnew.domain.model.TimeFilter
import com.example.todoappnew.domain.usecase.ExportReportUseCase
import com.example.todoappnew.domain.usecase.GetAnalyticsUseCase
import com.example.todoappnew.domain.usecase.GetBackgroundUseCase
import com.example.todoappnew.domain.usecase.GetCategoryStatsUseCase
import com.example.todoappnew.domain.usecase.GetProductivityInsightsUseCase
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
    private val getProductivityInsightsUseCase: GetProductivityInsightsUseCase,
    private val getCategoryStatsUseCase: GetCategoryStatsUseCase,
    private val exportReportUseCase: ExportReportUseCase,
    private val getBackgroundUseCase: GetBackgroundUseCase
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow(TimeFilter.TODAY)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<DashboardUiState> = combine(
        _selectedFilter,
        getBackgroundUseCase()
    ) { filter, background ->
        Pair(filter, background)
    }.flatMapLatest { (filter, background) ->
        combine(
            getAnalyticsUseCase(filter),
            getProgressTrendUseCase(),
            getCategoryStatsUseCase(),
            getProductivityInsightsUseCase()
        ) { analytics, trend, categories, insights ->
            DashboardUiState(
                analyticsData = analytics,
                weeklyProgress = trend,
                categoryStats = categories,
                productivityInsights = insights,
                selectedFilter = filter,
                background = background,
                isLoading = false
            )
        }.onStart {
            emit(DashboardUiState(selectedFilter = filter, background = background, isLoading = true))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState(isLoading = true)
    )

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.FilterChanged -> {
                _selectedFilter.value = event.filter
            }
            is DashboardEvent.ExportReport -> {
                exportReport(event.context, event.format)
            }
        }
    }

    private fun exportReport(context: Context, format: ExportFormat) {
        val currentState = uiState.value
        if (currentState.isLoading) return

        viewModelScope.launch {
            try {
                // Production-ready export logic
                val reportContent = exportReportUseCase(
                    data = currentState.analyticsData,
                    timeFilterName = currentState.selectedFilter.name
                )
                
                val extension = if (format == ExportFormat.PDF) "pdf" else "csv"
                val fileName = "Todo_Analytics_Report_${System.currentTimeMillis()}.$extension"
                val file = File(context.cacheDir, fileName)
                file.writeText(reportContent)
                
                ShareUtils.shareFile(context, file, "Share Analytics Report")
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

sealed class DashboardEvent {
    data class FilterChanged(val filter: TimeFilter) : DashboardEvent()
    data class ExportReport(val context: Context, val format: ExportFormat) : DashboardEvent()
}

enum class ExportFormat {
    PDF, CSV
}
