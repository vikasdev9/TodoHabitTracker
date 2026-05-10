package com.example.todoappnew.presentation.ui.screens.dashboard

import com.example.todoappnew.domain.model.AnalyticsData
import com.example.todoappnew.domain.model.CategoryStats
import com.example.todoappnew.domain.model.ProductivityInsights
import com.example.todoappnew.domain.model.ProgressData
import com.example.todoappnew.domain.model.TimeFilter
import com.example.todoappnew.domain.model.AppBackground

data class DashboardUiState(
    val analyticsData: AnalyticsData = AnalyticsData(0, 0, 0, 0),
    val weeklyProgress: List<ProgressData> = emptyList(),
    val categoryStats: List<CategoryStats> = emptyList(),
    val productivityInsights: ProductivityInsights = ProductivityInsights(),
    val selectedFilter: TimeFilter = TimeFilter.TODAY,
    val background: AppBackground = AppBackground.AURORA,
    val isLoading: Boolean = false
)
