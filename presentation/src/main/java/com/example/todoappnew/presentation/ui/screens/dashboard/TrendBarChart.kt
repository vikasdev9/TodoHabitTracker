package com.example.todoappnew.presentation.ui.screens.dashboard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoappnew.domain.model.ProgressData

@Composable
fun TrendBarChart(
    data: List<ProgressData>,
    modifier: Modifier = Modifier
) {
    val maxTasks = data.maxOfOrNull { it.totalCount }?.takeIf { it > 0 } ?: 1
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(data) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(1f, tween(1000))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Weekly Activity",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val barWidth = size.width / (data.size * 1.5f)
                val spacing = (size.width - (barWidth * data.size)) / (data.size + 1)

                data.forEachIndexed { index, day ->
                    val totalBarHeight = (day.totalCount.toFloat() / maxTasks) * size.height
                    val completedBarHeight = (day.completedCount.toFloat() / maxTasks) * size.height * animationProgress.value
                    
                    val x = spacing + index * (barWidth + spacing)
                    
                    // Draw Total Background (Shadow)
                    drawRoundRect(
                        color = Color.LightGray.copy(alpha = 0.2f),
                        topLeft = Offset(x, size.height - totalBarHeight),
                        size = Size(barWidth, totalBarHeight),
                        cornerRadius = CornerRadius(4.dp.toPx())
                    )

                    // Draw Completed Bar
                    drawRoundRect(
                        color = Color(0xFF4CAF50),
                        topLeft = Offset(x, size.height - completedBarHeight),
                        size = Size(barWidth, completedBarHeight),
                        cornerRadius = CornerRadius(4.dp.toPx())
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            data.forEach { day ->
                Text(
                    text = day.label,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
