package com.example.todoappnew.presentation.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.todoappnew.domain.model.Task
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ShareUtils {

    fun shareFile(context: Context, file: File, title: String) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(intent, title)
        context.startActivity(chooser)
    }

    /**
     * Shares a task reminder with friends.
     * @param context Android context
     * @param task The task to share
     * @param packageName Optional package name for specific apps (e.g., "com.whatsapp")
     */
    fun shareTaskWithFriend(context: Context, task: Task, packageName: String? = null) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        val formattedDate = if (task.scheduledTime > 0) {
            dateFormat.format(Date(task.scheduledTime))
        } else {
            "some time"
        }

        val shareText = "Hey, reminder: ${task.title} at $formattedDate"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            packageName?.let {
                setPackage(it)
            }
        }

        try {
            if (packageName != null) {
                context.startActivity(intent)
            } else {
                val chooser = Intent.createChooser(intent, "Share Task with Friend")
                context.startActivity(chooser)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to share task", Toast.LENGTH_SHORT).show()
        }
    }
}
