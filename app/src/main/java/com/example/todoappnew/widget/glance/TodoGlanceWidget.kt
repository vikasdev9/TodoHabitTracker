package com.example.todoappnew.widget.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
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
                .padding(16.dp)
                .clickable(actionStartActivity<MainActivity>())
        ) {
            // Header: "Today [count] +"
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Today",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = GlanceTheme.colors.primary
                        )
                    )
                    Spacer(GlanceModifier.width(8.dp))
                    Text(
                        text = tasks.size.toString(),
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = GlanceTheme.colors.onSurfaceVariant
                        )
                    )
                }
                
                Spacer(GlanceModifier.defaultWeight())
                
                // Add Task Button
                Box(
                    modifier = GlanceModifier
                        .size(32.dp)
                        .background(GlanceTheme.colors.primaryContainer)
                        .clickable(actionStartActivity<MainActivity>()),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = GlanceTheme.colors.onPrimaryContainer
                        )
                    )
                }
            }
            
            Spacer(GlanceModifier.height(16.dp))
            
            if (tasks.isEmpty()) {
                Box(
                    modifier = GlanceModifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "All tasks completed! ✨",
                        style = TextStyle(
                            color = GlanceTheme.colors.onSurfaceVariant,
                            fontSize = 14.sp
                        )
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
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Minimalist Square Checkbox
            Box(
                modifier = GlanceModifier
                    .size(18.dp)
                    .background(GlanceTheme.colors.secondaryContainer)
            ) {
            }
            
            Spacer(GlanceModifier.width(12.dp))
            
            Text(
                text = task.title,
                maxLines = 1,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = GlanceTheme.colors.onSurface
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
