package com.example.todoappnew.data.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.todoappnew.data.local.dao.TaskDao
import com.example.todoappnew.domain.model.TaskStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlarmActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var taskDao: TaskDao

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val taskId = intent?.getStringExtra(NotificationHelper.EXTRA_TASK_ID) ?: ""
        
        println("AlarmActionReceiver: Received action: $action for task: $taskId")

        when (action) {
            NotificationHelper.ACTION_STOP_ALARM -> {
                alarmManager.stopAlarm()
            }
            NotificationHelper.ACTION_MARK_COMPLETED -> {
                alarmManager.stopAlarm()
                if (taskId.isNotBlank()) {
                    notificationHelper.cancelNotification(taskId.hashCode())
                    CoroutineScope(Dispatchers.IO).launch {
                        val task = taskDao.getTaskById(taskId)
                        task?.let {
                            taskDao.updateTask(it.copy(
                                isCompleted = true,
                                isSynced = false, // Trigger re-sync
                                status = TaskStatus.COMPLETED
                            ))
                        }
                    }
                }
            }
        }
    }
}
