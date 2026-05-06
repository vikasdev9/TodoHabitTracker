package com.example.todoappnew.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.todoappnew.R
import com.example.todoappnew.data.local.dao.TaskDao
import com.example.todoappnew.data.local.entity.TaskEntity
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class TodoWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return TodoRemoteViewsFactory(applicationContext)
    }
}

class TodoRemoteViewsFactory(
    private val context: Context
) : RemoteViewsService.RemoteViewsFactory {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetEntryPoint {
        fun taskDao(): TaskDao
    }

    private lateinit var taskDao: TaskDao
    private var tasks: List<TaskEntity> = emptyList()

    override fun onCreate() {
        val hiltEntryPoint = EntryPoints.get(context, WidgetEntryPoint::class.java)
        taskDao = hiltEntryPoint.taskDao()
    }

    override fun onDataSetChanged() {
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

        runBlocking {
            tasks = taskDao.getTodayTasksSync(startOfDay, endOfDay)
        }
    }

    override fun onDestroy() {}

    override fun getCount(): Int = tasks.size

    override fun getViewAt(position: Int): RemoteViews {
        if (position >= tasks.size) return RemoteViews(context.packageName, R.layout.widget_item)

        val task = tasks[position]
        val views = RemoteViews(context.packageName, R.layout.widget_item).apply {
            setTextViewText(R.id.item_title, task.title)
            
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val time = if (task.scheduledTime > 0) {
                dateFormat.format(Date(task.scheduledTime))
            } else {
                "--:--"
            }
            setTextViewText(R.id.item_time, time)
        }

        return views
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = true
}
