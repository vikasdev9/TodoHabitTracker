package com.example.todoappnew.domain.usecase

import com.example.todoappnew.domain.model.AnalyticsData
import com.example.todoappnew.domain.model.TimeFilter
import com.example.todoappnew.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class GetAnalyticsUseCase @Inject constructor(
    private val repository: TaskRepository
) {

    operator fun invoke(timeFilter: TimeFilter): Flow<AnalyticsData> {
        return repository.getTasks().map { tasks ->
            val currentTime = System.currentTimeMillis()
            
            val filteredByTime = tasks.filter { task ->
                isWithinTimeRange(task.scheduledTime, timeFilter)
            }

            AnalyticsData(
                total = filteredByTime.size,
                completed = filteredByTime.count { it.isCompleted },
                pending = filteredByTime.count { !it.isCompleted && it.scheduledTime >= currentTime },
                missed = filteredByTime.count { !it.isCompleted && it.scheduledTime < currentTime }
            )
        }
    }

    private fun isWithinTimeRange(scheduledTime: Long, filter: TimeFilter): Boolean {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis
        
        return when (filter) {
            TimeFilter.TODAY -> {
                val taskCalendar = Calendar.getInstance().apply { timeInMillis = scheduledTime }
                taskCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                taskCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)
            }
            TimeFilter.WEEK -> {
                // Last 7 days
                val sevenDaysAgo = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -7)
                }.timeInMillis
                scheduledTime in sevenDaysAgo..currentTime
            }
            TimeFilter.MONTH -> {
                // Last 30 days
                val thirtyDaysAgo = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -30)
                }.timeInMillis
                scheduledTime in thirtyDaysAgo..currentTime
            }
            TimeFilter.YEAR -> {
                // Last 365 days
                val oneYearAgo = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, -365)
                }.timeInMillis
                scheduledTime in oneYearAgo..currentTime
            }
        }
    }
}
