package com.example.todoappnew.data.repository

import android.content.Context
import androidx.work.*
import com.example.todoappnew.data.local.dao.TaskDao
import com.example.todoappnew.data.local.datastore.PreferencesDataSource
import com.example.todoappnew.data.mapper.toDomain
import com.example.todoappnew.data.mapper.toEntity
import com.example.todoappnew.data.worker.CloudSyncWorker
import com.example.todoappnew.data.worker.TaskReminderWorker
import com.example.todoappnew.domain.model.Task
import com.example.todoappnew.domain.model.TaskStatus
import com.example.todoappnew.domain.repository.TaskRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val preferencesDataSource: PreferencesDataSource,
    @ApplicationContext private val context: Context
) : TaskRepository {

    private val workManager = WorkManager.getInstance(context)

    override fun getTasks(): Flow<List<Task>> {
        return taskDao.getTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchTasks(query: String): Flow<List<Task>> {
        return taskDao.searchTasks(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun filterTasks(status: TaskStatus): Flow<List<Task>> {
        return taskDao.filterTasks(status).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertTask(task: Task) {
        val entity = task.toEntity()
        taskDao.insertTask(entity)
        scheduleTaskReminder(task)
        updateWidget()
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
        if (task.isCompleted) {
            cancelTaskReminder(task.id)
            triggerCloudSync()
        } else {
            scheduleTaskReminder(task)
        }
        updateWidget()
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task.toEntity())
        cancelTaskReminder(task.id)
        updateWidget()
    }

    private fun updateWidget() {
        val intent = android.content.Intent("com.example.todoappnew.widget.ACTION_REFRESH").apply {
            setPackage(context.packageName)
        }
        context.sendBroadcast(intent)
        
        // Update Glance Widget
        android.content.Intent("androidx.glance.appwidget.action.UPDATE").apply {
            setPackage(context.packageName)
            context.sendBroadcast(this)
        }
    }

    private suspend fun scheduleTaskReminder(task: Task) {
        if (task.isCompleted || task.scheduledTime == 0L) {
            cancelTaskReminder(task.id)
            return
        }

        val currentTime = System.currentTimeMillis()
        val delay = task.scheduledTime - currentTime

        if (delay <= 0) {
            // Time already passed, mark as MISSED
            val missedTask = task.toEntity().copy(status = TaskStatus.MISSED)
            taskDao.updateTask(missedTask)
            cancelTaskReminder(task.id)
            return
        }

        val workRequest = OneTimeWorkRequestBuilder<TaskReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("taskId" to task.id))
            .addTag("task_${task.id}")
            .build()

        workManager.enqueueUniqueWork(
            "task_${task.id}",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun cancelTaskReminder(taskId: String) {
        workManager.cancelUniqueWork("task_$taskId")
    }

    private fun triggerCloudSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<CloudSyncWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
            .build()

        workManager.enqueueUniqueWork(
            "cloud_sync",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            syncRequest
        )
    }

    override fun getDefaultFilter(): Flow<TaskStatus> = preferencesDataSource.defaultFilter
    override suspend fun updateDefaultFilter(status: TaskStatus) = preferencesDataSource.updateDefaultFilter(status)
}
