package com.example.todoappnew.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todoappnew.presentation.ui.screens.home.HomeScreen
import com.example.todoappnew.presentation.ui.screens.task_detail.TaskDetailScreen
import com.example.todoappnew.presentation.ui.screens.settings.SettingsScreen
import com.example.todoappnew.presentation.ui.screens.dashboard.DashboardScreen
import com.example.todoappnew.presentation.ui.screens.calendar.CalendarScreen
import com.example.todoappnew.presentation.ui.screens.profile.ProfileScreen

@Composable
fun TodoNavHost(
    navController: NavHostController,
    onOpenDrawer: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = hiltViewModel(),
                onNavigateToDetail = { taskId ->
                    navController.navigate(Screen.TaskDetail.passTaskId(taskId))
                },
                onOpenDrawer = onOpenDrawer
            )
        }
        composable(
            route = Screen.TaskDetail.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            TaskDetailScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = hiltViewModel(),
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                viewModel = hiltViewModel(),
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.Calendar.route) {
            CalendarScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}
