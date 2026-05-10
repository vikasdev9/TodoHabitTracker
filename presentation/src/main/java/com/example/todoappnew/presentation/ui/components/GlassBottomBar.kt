package com.example.todoappnew.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Data class for navigation items
 */
data class NavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

/**
 * A floating glassmorphism bottom navigation bar with neon effects and animations.
 */
@Composable
fun GlassBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem("home", Icons.Rounded.Home, "Home"),
        NavItem("calendar", Icons.Rounded.CalendarMonth, "Calendar"),
        NavItem("stats", Icons.Rounded.BarChart, "Stats"),
        NavItem("profile", Icons.Rounded.Person, "Profile")
    )

    // Colors
    val neonPink = Color(0xFFFF4D80)
    val glassWhite = Color(0x1AFFFFFF)
    val glassBorder = Color(0x4DFFFFFF)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .navigationBarsPadding()
    ) {
        // Main Container
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = neonPink.copy(alpha = 0.3f),
                    ambientColor = Color.Black.copy(alpha = 0.5f)
                )
                // Glassmorphism effect
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            glassWhite.copy(alpha = 0.12f),
                            glassWhite.copy(alpha = 0.04f)
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(glassBorder, Color.Transparent)
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                
                // Animated selection states
                val tint by animateColorAsState(
                    targetValue = if (isSelected) neonPink else Color.White.copy(alpha = 0.4f),
                    animationSpec = tween(300),
                    label = "IconTint"
                )
                
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.25f else 1.0f,
                    animationSpec = tween(300),
                    label = "IconScale"
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null // Disable ripple for premium feel
                        ) { onNavigate(item.route) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = tint,
                        modifier = Modifier
                            .size(26.dp)
                            .scale(scale)
                    )
                    
                    if (isSelected) {
                        Spacer(modifier = Modifier.height(4.dp))
                        // Neon Active Indicator (Pill)
                        Box(
                            modifier = Modifier
                                .size(width = 12.dp, height = 3.dp)
                                .background(neonPink, RoundedCornerShape(2.dp))
                                .shadow(elevation = 8.dp, spotColor = neonPink)
                        )
                    }
                }
            }
        }
    }
}
