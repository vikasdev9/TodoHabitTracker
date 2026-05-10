package com.example.todoappnew.domain.model

data class ProductivityInsights(
    val completionPercentage: Float = 0f,
    val productivityScore: Int = 0,
    val longestStreak: Int = 0,
    val currentStreak: Int = 0,
    val mostProductiveDay: String = "N/A",
    val upcomingTasksNext7Days: Int = 0,
    val averageCompletionTime: String = "N/A",
    val missedTaskRatio: Float = 0f,
    val mostActiveCategory: String = "Personal"
)
