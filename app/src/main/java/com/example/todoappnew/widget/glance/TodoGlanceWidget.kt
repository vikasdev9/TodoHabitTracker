package com.example.todoappnew.widget.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.todoappnew.MainActivity
import com.example.todoappnew.data.local.dao.TaskDao
import com.example.todoappnew.data.local.entity.TaskEntity
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class TodoGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val taskDao = EntryPoints.get(context, WidgetEntryPoint::class.java).taskDao()
        
        provideContent {
            GlanceTheme {
                val tasks = fetchTodayTasks(taskDao)
                WidgetContent(tasks)
            }
        }
    }

    private fun fetchTodayTasks(taskDao: TaskDao): List<TaskEntity> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfDay = calendar.timeInMillis

        return runBlocking {
            taskDao.getTodayTasksSync(startOfDay, endOfDay)
        }
    }

    @Composable
    private fun WidgetContent(tasks: List<TaskEntity>) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.surface)
                .padding(8.dp)
                .clickable(actionStartActivity<MainActivity>())
        ) {
            Text(
                text = "Today's Tasks",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = GlanceTheme.colors.onSurface
                )
            )
            
            Spacer(GlanceModifier.height(8.dp))
            
            if (tasks.isEmpty()) {
                Box(
                    modifier = GlanceModifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tasks for today",
                        style = TextStyle(color = GlanceTheme.colors.onSurface)
                    )
                }
            } else {
                LazyColumn {
                    items(tasks) { task ->
                        TaskItem(task)
                    }
                }
            }
        }
    }

    @Composable
    private fun TaskItem(task: TaskEntity) {
        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = task.title,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = GlanceTheme.colors.onSurface
                )
            )
            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(task.scheduledTime))
            Text(
                text = time,
                style = TextStyle(
                    color = GlanceTheme.colors.onSurface,
                    fontSize = 12.sp
                )
            )
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint {
        fun taskDao(): TaskDao
    }
}
