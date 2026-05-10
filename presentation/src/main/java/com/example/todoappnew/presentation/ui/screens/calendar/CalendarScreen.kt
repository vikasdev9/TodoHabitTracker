package com.example.todoappnew.presentation.ui.screens.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoappnew.presentation.ui.components.FuturisticTaskCard
import com.example.todoappnew.presentation.ui.components.GlassCard
import com.example.todoappnew.presentation.ui.components.PremiumBackground
import com.example.todoappnew.presentation.ui.screens.settings.SettingsViewModel
import com.example.todoappnew.presentation.ui.theme.NeonPink
import dev.alejo.compose_calendar.ComposeCalendar
import dev.alejo.compose_calendar.CalendarEvent
import dev.alejo.compose_calendar.util.CalendarColors
import kotlinx.datetime.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val settingsState by settingsViewModel.uiState.collectAsState()

    PremiumBackground(background = settingsState.background) {
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(20.dp)
                ) {
                    item {
                        CalendarHeader()
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    item {
                        ModernCalendarCard(
                            selectedDate = uiState.selectedDate,
                            onDateSelected = viewModel::onDateSelected,
                            allTasks = uiState.allTasks
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    item {
                        Text(
                            text = formatSelectedDate(uiState.selectedDate).uppercase(),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            ),
                            color = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (uiState.tasks.isEmpty()) {
                        item {
                            EmptyTasksPlaceholder()
                        }
                    } else {
                        items(uiState.tasks, key = { it.id }) { task ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + slideInVertically { it / 2 },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                FuturisticTaskCard(
                                    title = task.title,
                                    time = formatTime(task.scheduledTime),
                                    category = "Task",
                                    status = task.status,
                                    onStatusChange = { viewModel.toggleTaskCompletion(task) }
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarHeader() {
    Column(modifier = Modifier.padding(top = 40.dp)) {
        Text(
            text = "Calendar",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = (-1).sp,
                fontSize = 36.sp
            ),
            color = Color.White
        )
        Text(
            text = "Plan your days at a glance.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ModernCalendarCard(
    selectedDate: kotlinx.datetime.LocalDate,
    onDateSelected: (kotlinx.datetime.LocalDate) -> Unit,
    allTasks: List<com.example.todoappnew.domain.model.Task>
) {
    // Convert selectedDate to java.time.LocalDate
    val javaSelectedDate = LocalDate.of(selectedDate.year, selectedDate.month.number, selectedDate.dayOfMonth)
    
    // Map all tasks to CalendarEvent for indicators
    val events = allTasks.map { task ->
        val instant = Instant.fromEpochMilliseconds(task.scheduledTime)
        val taskDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        CalendarEvent(
            data = task,
            date = LocalDate.of(taskDate.year, taskDate.monthNumber, taskDate.dayOfMonth)
        )
    }

    GlassCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 32.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.White.copy(alpha = 0.03f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            ) {
                ComposeCalendar(
                    initDate = javaSelectedDate,
                    onDayClick = { date, _ ->
                        onDateSelected(
                            kotlinx.datetime.LocalDate(
                                date.year,
                                date.monthValue,
                                date.dayOfMonth
                            )
                        )
                    },
                    events = events,
                    calendarColors = CalendarColors(
                        backgroundColor = Color.Transparent,
                        contentColor = Color.White,
                        headerBackgroundColor = Color.Transparent,
                        headerContentColor = Color.White,
                        navigationContainerColor = Color.White.copy(alpha = 0.05f),
                        navigationContentColor = Color.White,
                        navigationDisableContainerColor = Color.Transparent,
                        navigationDisableContentColor = Color.White.copy(alpha = 0.2f),
                        eventBackgroundColor = NeonPink,
                        eventContentColor = Color.White
                    ),
                    eventIndicator = { _, position, _ ->
                        EventIndicatorDot(position)
                    }
                )
            }
        }
    }
}

@Composable
fun EventIndicatorDot(position: Int) {
    if (position < 3) {
        Box(
            modifier = Modifier
                .padding(horizontal = 1.dp)
                .size(4.dp)
                .clip(CircleShape)
                .background(NeonPink.copy(alpha = 0.8f))
        )
    }
}

@Composable
fun EmptyTasksPlaceholder() {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        cornerRadius = 24.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Nothing scheduled.",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.5f)
                )
                Text(
                    text = "Take a break or plan something new.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.3f)
                )
            }
        }
    }
}

private fun formatSelectedDate(date: kotlinx.datetime.LocalDate): String {
    val dayName = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    val monthName = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$dayName, $monthName ${date.dayOfMonth}"
}

private fun formatTime(timeMillis: Long): String {
    return try {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        sdf.format(Date(timeMillis))
    } catch (e: Exception) {
        "00:00"
    }
}
