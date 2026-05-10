package com.example.todoappnew.presentation.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todoappnew.domain.model.TaskStatus
import com.example.todoappnew.presentation.ui.components.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToDetail: (String) -> Unit,
    onOpenDrawer: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    
    val categories = listOf("All", "Personal", "Work", "Health")
    var selectedCategory by remember { mutableStateOf("All") }

    val currentDate = remember { 
        SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date()) 
    }

    // Split tasks for Today and Upcoming
    val todayTasks = state.tasks.filter { it.status != TaskStatus.COMPLETED }.take(2)
    val upcomingTasks = state.tasks.filter { it.status != TaskStatus.COMPLETED }.drop(2)

    val completedCount = state.tasks.count { it.status == TaskStatus.COMPLETED }
    val totalCount = state.tasks.size
    val completionPercentage = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    PremiumBackground(background = state.background) {
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                NeonFAB(onClick = { onNavigateToDetail("") })
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                DashboardHeader(
                    date = currentDate,
                    userName = "vikaschauhan0368",
                    completionPercentage = completionPercentage,
                    completedCount = completedCount,
                    totalCount = totalCount
                )

                SummaryPill(taskCount = state.tasks.count { it.status != TaskStatus.COMPLETED })

                ModernCategoryChips(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    item { SectionHeader(title = "TODAY", count = todayTasks.size) }
                    items(todayTasks) { task ->
                        FuturisticTaskCard(
                            modifier = Modifier.clickable { onNavigateToDetail(task.id) },
                            title = task.title,
                            time = "Today, 9:00 AM",
                            category = "Personal",
                            status = task.status,
                            isRepeating = true,
                            onStatusChange = { viewModel.onEvent(HomeEvent.ToggleTaskCompletion(task)) }
                        )
                    }

                    item { SectionHeader(title = "UPCOMING", count = upcomingTasks.size) }
                    items(upcomingTasks) { task ->
                        FuturisticTaskCard(
                            modifier = Modifier.clickable { onNavigateToDetail(task.id) },
                            title = task.title,
                            time = "May 12, 9:00 AM",
                            category = "Work",
                            status = task.status,
                            isRepeating = true,
                            onStatusChange = { viewModel.onEvent(HomeEvent.ToggleTaskCompletion(task)) }
                        )
                    }
                }
            }
        }
    }
}
