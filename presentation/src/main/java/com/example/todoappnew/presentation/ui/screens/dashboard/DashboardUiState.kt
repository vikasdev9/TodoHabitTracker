package com.example.todoappnew.presentation.ui.screens.dashboard

import com.example.todoappnew.domain.model.AnalyticsData
import com.example.todoappnew.domain.model.ProgressData
import com.example.todoappnew.domain.model.TimeFilter

data class DashboardUiState(
    val analyticsData: AnalyticsData = AnalyticsData(0, 0, 0, 0),
    val weeklyProgress: List<ProgressData> = emptyList(),
    val selectedFilter: TimeFilter = TimeFilter.TODAY,
    val isLoading: Boolean = false
)
