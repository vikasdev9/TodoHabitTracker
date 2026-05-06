package com.example.todoappnew.data.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.todoappnew.domain.repository.TaskRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RebootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: TaskRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                val tasks = repository.getTasks().first()
                tasks.forEach { task ->
                    if (!task.isCompleted && task.scheduledTime > System.currentTimeMillis()) {
                        repository.updateTask(task) // Trigger re-scheduling in repository
                    }
                }
            }
        }
    }
}
