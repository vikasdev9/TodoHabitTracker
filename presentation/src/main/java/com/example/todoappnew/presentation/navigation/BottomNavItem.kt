package com.example.todoappnew.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Screen.Home.route, "Home", Icons.Rounded.Home)
    object Calendar : BottomNavItem(Screen.Calendar.route, "Calendar", Icons.Rounded.DateRange)
    object Stats : BottomNavItem(Screen.Dashboard.route, "Stats", Icons.Rounded.BarChart)
    object Profile : BottomNavItem(Screen.Profile.route, "Profile", Icons.Rounded.Person)
}
