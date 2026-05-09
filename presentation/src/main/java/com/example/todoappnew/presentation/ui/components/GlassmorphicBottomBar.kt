package com.example.todoappnew.presentation.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.todoappnew.presentation.navigation.BottomNavItem
import com.example.todoappnew.presentation.ui.theme.NeonPink

@Composable
fun GlassmorphicBottomBar(
    navController: NavController
) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Calendar,
        BottomNavItem.Stats,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                horizontal = 18.dp,
                vertical = 12.dp
            ),
        contentAlignment = Alignment.BottomCenter
    ) {

        // Main Glass Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .graphicsLayer {
                    alpha = 0.96f
                }
                .shadow(
                    elevation = 30.dp,
                    shape = RoundedCornerShape(24.dp),
                    spotColor = NeonPink.copy(alpha = 0.18f),
                    ambientColor = NeonPink.copy(alpha = 0.12f)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF4E7F5).copy(alpha = 0.92f),
                            Color(0xFFEAD8EC).copy(alpha = 0.95f)
                        )
                    )
                )
        )

        // Border Glow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Color.White.copy(alpha = 0.04f)
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .padding(horizontal = 8.dp),

            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEach { item ->

                val isSelected = currentRoute == item.route

                BottomBarItem(
                    item = item,
                    isSelected = isSelected,
                    onClick = {

                        if (currentRoute != item.route) {

                            navController.navigate(item.route) {

                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RowScope.BottomBarItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val contentColor by animateColorAsState(
        targetValue =
            if (isSelected) {
                NeonPink
            } else {
                Color.Black.copy(alpha = 0.65f)
            },

        animationSpec = tween(300),
        label = "BottomBarColor"
    )

    val iconScale by animateFloatAsState(
        targetValue =
            if (isSelected) 1.15f
            else 1f,

        animationSpec = tween(300),
        label = "BottomBarScale"
    )

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null
            ) {
                onClick()
            },

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Selected Icon Background
        Box(
            modifier = Modifier
                .size(
                    if (isSelected) 38.dp else 30.dp
                )
                .clip(CircleShape)
                .background(
                    if (isSelected) {
                        NeonPink.copy(alpha = 0.18f)
                    } else {
                        Color.Transparent
                    }
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = contentColor,

                modifier = Modifier
                    .size(22.dp)
                    .scale(iconScale)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.title,
            color = contentColor,
            fontSize = 10.sp,

            fontWeight =
                if (isSelected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                },

            style = MaterialTheme.typography.labelSmall
        )
    }
}