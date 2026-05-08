package com.example.todoappnew.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Dashboard : Screen("dashboard")
    object Calendar : Screen("calendar")
    object Profile : Screen("profile")
    object TaskDetail : Screen("task_detail?taskId={taskId}") {
        fun passTaskId(taskId: String = ""): String {
            return "task_detail?taskId=$taskId"
        }
    }
    object Settings : Screen("settings")
    object About : Screen("about")
}
