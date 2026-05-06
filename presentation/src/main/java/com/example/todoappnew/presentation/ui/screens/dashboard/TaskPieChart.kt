package com.example.todoappnew.presentation.ui.screens.dashboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.todoappnew.domain.model.AnalyticsData

@Composable
fun TaskPieChart(
    data: AnalyticsData,
    modifier: Modifier = Modifier
) {
    val total = data.completed + data.pending + data.missed
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(data) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    val completedColor = Color(0xFF4CAF50)
    val pendingColor = Color(0xFFFF9800)
    val missedColor = MaterialTheme.colorScheme.error

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
            Canvas(modifier = Modifier.size(180.dp)) {
                if (total == 0) {
                    drawArc(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
                    )
                } else {
                    val sweepCompleted = (data.completed.toFloat() / total) * 360f * animationProgress.value
                    val sweepPending = (data.pending.toFloat() / total) * 360f * animationProgress.value
                    val sweepMissed = (data.missed.toFloat() / total) * 360f * animationProgress.value

                    var startAngle = -90f

                    // Draw Completed
                    drawArc(
                        color = completedColor,
                        startAngle = startAngle,
                        sweepAngle = sweepCompleted,
                        useCenter = false,
                        style = Stroke(width = 24.dp.toPx(), cap = StrokeCap.Round)
                    )
                    startAngle += sweepCompleted

                    // Draw Pending
                    drawArc(
                        color = pendingColor,
                        startAngle = startAngle,
                        sweepAngle = sweepPending,
                        useCenter = false,
                        style = Stroke(width = 24.dp.toPx(), cap = StrokeCap.Round)
                    )
                    startAngle += sweepPending

                    // Draw Missed
                    drawArc(
                        color = missedColor,
                        startAngle = startAngle,
                        sweepAngle = sweepMissed,
                        useCenter = false,
                        style = Stroke(width = 24.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$total",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Total Tasks",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ChartLegend(
            entries = listOf(
                ChartLegendEntry("Completed", completedColor),
                ChartLegendEntry("Pending", pendingColor),
                ChartLegendEntry("Missed", missedColor)
            )
        )
    }
}

@Composable
fun ChartLegend(entries: List<ChartLegendEntry>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        entries.forEach { entry ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(entry.color, CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = entry.label, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

data class ChartLegendEntry(val label: String, val color: Color)
