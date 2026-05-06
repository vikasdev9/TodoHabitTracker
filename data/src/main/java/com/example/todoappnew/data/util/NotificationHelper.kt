package com.example.todoappnew.data.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "task_reminder_channel"
        const val ACTION_STOP_ALARM = "STOP_ALARM"
        const val ACTION_MARK_COMPLETED = "MARK_COMPLETED"
        const val EXTRA_TASK_ID = "extra_task_id"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Task Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for scheduled tasks"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(taskId: Int, title: String, message: String, stringTaskId: String) {
        val stopIntent = Intent(context, AlarmActionReceiver::class.java).apply {
            action = ACTION_STOP_ALARM
            putExtra(EXTRA_TASK_ID, stringTaskId)
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            context, taskId, stopIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val completedIntent = Intent(context, AlarmActionReceiver::class.java).apply {
            action = ACTION_MARK_COMPLETED
            putExtra(EXTRA_TASK_ID, stringTaskId)
        }
        val completedPendingIntent = PendingIntent.getBroadcast(
            context, taskId, completedIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop Alarm", stopPendingIntent)
            .addAction(android.R.drawable.ic_menu_save, "Complete", completedPendingIntent)
            .build()

        notificationManager.notify(taskId, notification)
    }

    fun cancelNotification(taskId: Int) {
        notificationManager.cancel(taskId)
    }
}
