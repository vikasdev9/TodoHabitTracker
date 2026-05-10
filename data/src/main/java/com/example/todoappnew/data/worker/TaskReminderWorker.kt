package com.example.todoappnew.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todoappnew.data.local.dao.TaskDao
import com.example.todoappnew.data.util.AlarmManager
import com.example.todoappnew.data.util.NotificationHelper
import com.example.todoappnew.domain.model.TaskStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class TaskReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val taskDao: TaskDao,
    private val notificationHelper: NotificationHelper,
    private val alarmManager: AlarmManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val taskId = inputData.getString("taskId") ?: return@withContext Result.failure()

        val entity = taskDao.getTaskById(taskId) ?: return@withContext Result.success()
        
        // Only notify if the task is not completed and not already missed
        if (entity.isCompleted || entity.status == TaskStatus.MISSED) {
            return@withContext Result.success()
        }

        // Update status to RUNNING
        taskDao.updateTask(entity.copy(status = TaskStatus.RUNNING))

        // Play alarm sound
        alarmManager.playAlarm()

        // Show high priority notification
        notificationHelper.showNotification(
            taskId = entity.id.hashCode(),
            title = "Task Reminder: ${entity.title}",
            message = entity.description,
            stringTaskId = entity.id
        )

        Result.success()
    }
}
