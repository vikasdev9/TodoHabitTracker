package com.example.todoappnew.domain.usecase

import com.example.todoappnew.domain.model.ProgressData
import com.example.todoappnew.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetProgressTrendUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<ProgressData>> {
        return repository.getTasks().map { tasks ->
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())
            val trendData = mutableListOf<ProgressData>()

            // Calculate for the last 7 days
            for (i in 6 downTo 0) {
                val dayCalendar = Calendar.getInstance()
                dayCalendar.add(Calendar.DAY_OF_YEAR, -i)
                
                val startOfDay = dayCalendar.apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis

                val endOfDay = dayCalendar.apply {
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                    set(Calendar.MILLISECOND, 999)
                }.timeInMillis

                val dayTasks = tasks.filter { it.scheduledTime in startOfDay..endOfDay }
                
                trendData.add(
                    ProgressData(
                        label = dateFormat.format(Date(startOfDay)),
                        completedCount = dayTasks.count { it.isCompleted },
                        totalCount = dayTasks.size
                    )
                )
            }
            trendData
        }
    }
}
