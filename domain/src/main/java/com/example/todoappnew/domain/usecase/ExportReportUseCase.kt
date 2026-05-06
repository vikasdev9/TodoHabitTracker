package com.example.todoappnew.domain.usecase

import com.example.todoappnew.domain.model.AnalyticsData
import javax.inject.Inject

class ExportReportUseCase @Inject constructor() {

    operator fun invoke(data: AnalyticsData, timeFilterName: String): String {
        val sb = StringBuilder()
        sb.append("Todo App Report - $timeFilterName\n")
        sb.append("Generated on: ${java.util.Date()}\n\n")
        sb.append("Category,Count\n")
        sb.append("Total Tasks,${data.total}\n")
        sb.append("Completed,${data.completed}\n")
        sb.append("Pending,${data.pending}\n")
        sb.append("Missed,${data.missed}\n")
        
        val completionRate = if (data.total > 0) (data.completed.toFloat() / data.total * 100).toInt() else 0
        sb.append("\nCompletion Rate,${completionRate}%\n")
        
        return sb.toString()
    }
}
