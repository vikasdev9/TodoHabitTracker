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
import com.example.todoappnew.domain.model.AnalyticsData

@Composable
fun TaskBarChart(
    data: AnalyticsData,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BarChartItem("Total", data.total, MaterialTheme.colorScheme.primary),
        BarChartItem("Done", data.completed, Color(0xFF4CAF50)),
        BarChartItem("Pending", data.pending, Color(0xFFFF9800)),
        BarChartItem("Missed", data.missed, MaterialTheme.colorScheme.error)
    )

    val maxValue = items.maxOfOrNull { it.value } ?: 1
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(data) {
        animationProgress.snapTo(0f)
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Status Comparison",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val barWidth = size.width / (items.size * 2f)
                val spacing = barWidth

                items.forEachIndexed { index, item ->
                    val barHeight = (item.value.toFloat() / maxValue) * size.height * animationProgress.value
                    val x = spacing + index * (barWidth + spacing)
                    val y = size.height - barHeight

                    drawRoundRect(
                        color = item.color,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight),
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
            items.forEach { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${item.value}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }
        }
    }
}

data class BarChartItem(
    val label: String,
    val value: Int,
    val color: Color
)
