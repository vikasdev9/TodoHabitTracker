package com.example.todoappnew.presentation.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoappnew.domain.model.Task
import com.example.todoappnew.domain.model.TaskStatus
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onDeleteClick: () -> Unit,
    onToggleCompletion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDeleteClick()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                Color.Red
            } else Color.Transparent
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
            }
        },
        content = {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { onTaskClick(task) },
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = task.isCompleted,
                        onCheckedChange = { onToggleCompletion() }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.weight(1f, fill = false)
                            )
                            task.stickerId?.let { sticker ->
                                Text(
                                    text = sticker,
                                    modifier = Modifier.padding(start = 8.dp),
                                    fontSize = 20.sp
                                )
                            }
                        }
                        if (task.description.isNotBlank()) {
                            Text(
                                text = task.description,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(task.scheduledTime)),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    StatusBadge(status = task.status)
                }
            }
        }
    )
}

@Composable
fun TaskGridItem(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onToggleCompletion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onTaskClick(task) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggleCompletion() },
                    modifier = Modifier.size(24.dp)
                )
                task.stickerId?.let {
                    Text(text = it, fontSize = 18.sp)
                }
            }

            Text(
                text = task.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 2,
                modifier = Modifier.weight(1f).padding(top = 4.dp)
            )

            Column {
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(task.scheduledTime)),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                StatusBadge(status = task.status)
            }
        }
    }
}

@Composable
fun TaskCompactItem(
    task: Task,
    onTaskClick: (Task) -> Unit,
    onToggleCompletion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onTaskClick(task) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggleCompletion() },
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = task.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                ),
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(task.scheduledTime)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun StatusBadge(status: TaskStatus) {
    val containerColor = when (status) {
        TaskStatus.PENDING -> Color(0xFFFFB74D)
        TaskStatus.RUNNING -> Color(0xFF64B5F6)
        TaskStatus.COMPLETED -> Color(0xFF81C784)
        TaskStatus.MISSED -> Color(0xFFE57373)
    }
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text(
            text = status.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = Color.White
        )
    }
}
