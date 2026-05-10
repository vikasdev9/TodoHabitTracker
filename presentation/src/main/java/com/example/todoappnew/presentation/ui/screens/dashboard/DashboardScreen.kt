package com.example.todoappnew.presentation.ui.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoappnew.domain.model.TimeFilter
import com.example.todoappnew.presentation.ui.components.PastelCard
import com.example.todoappnew.presentation.ui.components.PremiumBackground
import com.example.todoappnew.presentation.ui.theme.*
import com.example.todoappnew.domain.model.CategoryStats
import com.example.todoappnew.domain.model.ProductivityInsights
import androidx.compose.foundation.Canvas

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    PremiumBackground(background = state.background) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                DashboardTopBar(
                    onBackClick = onBackClick,
                    onExportClick = { viewModel.onEvent(DashboardEvent.ExportReport(context, ExportFormat.PDF)) }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                item {
                    DashboardHeaderSection()
                }

                item {
                    DashboardTimeFilterTabs(
                        selectedFilter = state.selectedFilter,
                        onFilterSelected = { viewModel.onEvent(DashboardEvent.FilterChanged(it)) }
                    )
                }

                item {
                    SummaryStatsSection(state)
                }

                item {
                    MainChartsSection(state)
                }

                item {
                    ProductivityInsightsSection(state.productivityInsights)
                }

                item {
                    CategoryBreakdownSection(state.categoryStats)
                }
                
                item {
                    WeeklyActivitySection(state.weeklyProgress)
                }

                item {
                    SmartInsightsSection()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(
    onBackClick: () -> Unit,
    onExportClick: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back", tint = PastelNavy)
            }
        },
        actions = {
            IconButton(
                onClick = onExportClick,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(CircleShape)
                    .background(PastelCream.copy(alpha = 0.5f))
            ) {
                Icon(Icons.Default.Download, contentDescription = "Export", tint = PastelNavy)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun DashboardHeaderSection() {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        Text(
            text = "Analytics Dashboard",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            ),
            color = PastelNavy
        )
        Text(
            text = "Track your productivity and task insights.",
            style = MaterialTheme.typography.bodyLarge,
            color = PastelSubHeading
        )
    }
}

@Composable
fun DashboardTimeFilterTabs(
    selectedFilter: TimeFilter,
    onFilterSelected: (TimeFilter) -> Unit
) {
    val filters = TimeFilter.entries
    
    PastelCard(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(56.dp),
        backgroundColor = PastelCreamAlt,
        cornerRadius = 28.dp,
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            filters.forEach { filter ->
                val isSelected = filter == selectedFilter
                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else Color.Transparent,
                    animationSpec = tween(300),
                    label = "TabBackground"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) PastelNavy else PastelSubHeading,
                    animationSpec = tween(300),
                    label = "TabContent"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(backgroundColor)
                        .then(if (isSelected) Modifier.shadow(2.dp, CircleShape) else Modifier)
                        .clickable { onFilterSelected(filter) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        ),
                        color = contentColor
                    )
                }
            }
        }
    }
}

@Composable
fun SummaryStatsSection(state: DashboardUiState) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AnalyticsSummaryCard(
                label = "Total",
                value = state.analyticsData.total,
                icon = Icons.AutoMirrored.Rounded.List,
                color = PastelBlue,
                modifier = Modifier.weight(1f)
            )
            AnalyticsSummaryCard(
                label = "Completed",
                value = state.analyticsData.completed,
                icon = Icons.Rounded.CheckCircle,
                color = PastelGreen,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AnalyticsSummaryCard(
                label = "Pending",
                value = state.analyticsData.pending,
                icon = Icons.Rounded.Pending,
                color = PastelOrange,
                modifier = Modifier.weight(1f)
            )
            AnalyticsSummaryCard(
                label = "Missed",
                value = state.analyticsData.missed,
                icon = Icons.Rounded.Error,
                color = PastelPink,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AnalyticsSummaryCard(
    label: String,
    value: Int,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animatedValue by animateIntAsState(
        targetValue = value,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "Counter"
    )

    PastelCard(
        modifier = modifier.height(110.dp),
        backgroundColor = PastelCream,
        cornerRadius = 24.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
                }
                
                Text(
                    text = animatedValue.toString(),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = PastelNavy
                )
            }
            
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = PastelSubHeading
            )
        }
    }
}

@Composable
fun MainChartsSection(state: DashboardUiState) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        PastelCard(
            modifier = Modifier.weight(1.2f).height(240.dp),
            backgroundColor = PastelCreamAlt,
            cornerRadius = 32.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Task Distribution",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = PastelNavy,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(140.dp)) {
                    AnalyticsDonutChart(
                        completed = state.analyticsData.completed.toFloat(),
                        pending = state.analyticsData.pending.toFloat(),
                        missed = state.analyticsData.missed.toFloat()
                    )
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.analyticsData.total.toString(),
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = PastelNavy
                        )
                        Text(
                            "Total",
                            style = MaterialTheme.typography.labelSmall,
                            color = PastelSubHeading
                        )
                    }
                }
            }
        }

        PastelCard(
            modifier = Modifier.weight(0.8f).height(240.dp),
            backgroundColor = PastelCreamAlt,
            cornerRadius = 32.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Productivity",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = PastelNavy,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.weight(1f))
                
                Text(
                    text = state.productivityInsights.productivityScore.toString(),
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 48.sp
                    ),
                    color = PastelPink
                )
                Text(
                    "SCORE",
                    style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
                    color = PastelSubHeading
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                LinearProgressIndicator(
                    progress = { state.productivityInsights.productivityScore / 100f },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = PastelPink,
                    trackColor = PastelCreamDark
                )
            }
        }
    }
}

@Composable
fun AnalyticsDonutChart(
    completed: Float,
    pending: Float,
    missed: Float
) {
    val total = completed + pending + missed
    if (total == 0f) return

    val completedAngle = (completed / total) * 360f
    val pendingAngle = (pending / total) * 360f

    val animateCompleted = animateFloatAsState(targetValue = completedAngle, animationSpec = tween(1000), label = "CompletedArc")
    val animatePending = animateFloatAsState(targetValue = pendingAngle, animationSpec = tween(1000), label = "PendingArc")

    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 35f
        
        drawArc(
            color = PastelGreen,
            startAngle = -90f,
            sweepAngle = animateCompleted.value,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        
        drawArc(
            color = PastelOrange,
            startAngle = -90f + animateCompleted.value,
            sweepAngle = animatePending.value,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
        
        drawArc(
            color = PastelPink,
            startAngle = -90f + animateCompleted.value + animatePending.value,
            sweepAngle = 360f - animateCompleted.value - animatePending.value,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun ProductivityInsightsSection(insights: ProductivityInsights) {
    Text(
        "Productivity Insights",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = PastelNavy,
        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 12.dp)
    )
    
    LazyRow(
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AnalyticsInsightCard(
                title = "Current Streak",
                value = "${insights.currentStreak} Days",
                icon = Icons.Rounded.Whatshot,
                color = PastelOrange
            )
        }
        item {
            AnalyticsInsightCard(
                title = "Longest Streak",
                value = "${insights.longestStreak} Days",
                icon = Icons.Rounded.EmojiEvents,
                color = PastelPurple
            )
        }
        item {
            AnalyticsInsightCard(
                title = "Completion",
                value = "${insights.completionPercentage.toInt()}%",
                icon = Icons.Rounded.DonutLarge,
                color = PastelBlue
            )
        }
        item {
            AnalyticsInsightCard(
                title = "Missed Ratio",
                value = "${(insights.missedTaskRatio * 100).toInt()}%",
                icon = Icons.AutoMirrored.Rounded.TrendingDown,
                color = PastelPink
            )
        }
    }
}

@Composable
fun AnalyticsInsightCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    PastelCard(
        modifier = Modifier.width(160.dp).height(100.dp),
        backgroundColor = PastelCream,
        cornerRadius = 24.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.weight(1f))
            Text(value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = PastelNavy)
            Text(title, style = MaterialTheme.typography.labelSmall, color = PastelSubHeading)
        }
    }
}

@Composable
fun CategoryBreakdownSection(categories: List<CategoryStats>) {
    Text(
        "Category Breakdown",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = PastelNavy,
        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 12.dp)
    )

    PastelCard(
        modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
        backgroundColor = PastelCream,
        cornerRadius = 32.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            categories.forEachIndexed { index, category ->
                AnalyticsCategoryRow(category)
                if (index < categories.size - 1) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun AnalyticsCategoryRow(category: CategoryStats) {
    val color = remember(category.category) {
        when (category.category) {
            "Work" -> PastelPurple
            "Personal" -> PastelPink
            "Health" -> PastelGreen
            "Study" -> PastelBlue
            else -> PastelOrange
        }
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(category.category, color = PastelNavy, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
            Text("${category.count} tasks", color = PastelSubHeading, style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { (category.count / 20f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = color,
            trackColor = PastelCreamDark
        )
    }
}

@Composable
fun WeeklyActivitySection(weeklyProgress: List<com.example.todoappnew.domain.model.ProgressData>) {
    Text(
        "Weekly Activity",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = PastelNavy,
        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 12.dp)
    )

    PastelCard(
        modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth().height(200.dp),
        backgroundColor = PastelCream,
        cornerRadius = 32.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            weeklyProgress.forEach { data ->
                AnalyticsBarItem(data)
            }
        }
    }
}

@Composable
fun AnalyticsBarItem(data: com.example.todoappnew.domain.model.ProgressData) {
    val barHeight = if (data.totalCount > 0) (data.completedCount.toFloat() / data.totalCount.toFloat()).coerceIn(0.1f, 1f) else 0.05f
    val animatedHeight by animateFloatAsState(targetValue = barHeight, animationSpec = tween(1000), label = "BarHeight")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(28.dp)
                .fillMaxHeight(0.75f * animatedHeight)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(PastelPurple, PastelPink)
                    )
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(data.label, color = PastelSubHeading, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun SmartInsightsSection() {
    Text(
        "Smart Insights",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = PastelNavy,
        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 12.dp)
    )

    Column(
        modifier = Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DashboardInsightAlertCard(
            title = "Most Productive Day",
            description = "You complete 40% more tasks on Tuesdays.",
            icon = Icons.Rounded.Lightbulb,
            color = PastelOrange
        )
        DashboardInsightAlertCard(
            title = "Morning Person",
            description = "Most of your tasks are finished before 11:00 AM.",
            icon = Icons.Rounded.WbSunny,
            color = PastelBlue
        )
        DashboardInsightAlertCard(
            title = "Upcoming Deadline",
            description = "You have 5 high priority tasks due in next 48 hours.",
            icon = Icons.Rounded.Warning,
            color = PastelPink
        )
    }
}

@Composable
fun DashboardInsightAlertCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color
) {
    PastelCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = PastelCream,
        cornerRadius = 24.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = PastelNavy)
                Text(description, style = MaterialTheme.typography.bodySmall, color = PastelSubHeading)
            }
        }
    }
}
