package com.example.todoappnew.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A modern glassmorphism task card with transparency, neon glow, and rich details.
 */
@Composable
fun GlassTaskCard(
    title: String,
    category: String,
    dateTime: String,
    isCompleted: Boolean,
    onToggleCompletion: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Colors for the Neon/Glass effect
    val neonCyan = Color(0xFF00FBFF)
    val glassWhite = Color(0x1AFFFFFF)
    val glassBorder = Color(0x33FFFFFF)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            // Outer Glow/Shadow
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = if (isCompleted) neonCyan.copy(alpha = 0.5f) else Color.Transparent,
                ambientColor = Color.Black.copy(alpha = 0.5f)
            )
            // Glass Background
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        glassWhite.copy(alpha = 0.15f),
                        glassWhite.copy(alpha = 0.05f)
                    )
                )
            )
            // Neon/Glass Border
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        if (isCompleted) neonCyan else glassBorder,
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Checkbox with Neon Tint
            IconButton(onClick = onToggleCompletion) {
                Icon(
                    imageVector = if (isCompleted) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                    contentDescription = "Toggle Completion",
                    tint = if (isCompleted) neonCyan else Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                    ),
                    color = if (isCompleted) Color.White.copy(alpha = 0.5f) else Color.White
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Date/Time
                    Text(
                        text = dateTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.5f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Category Badge
                    Surface(
                        color = if (isCompleted) Color.White.copy(alpha = 0.1f) else neonCyan.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = category.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.5.sp
                            ),
                            color = if (isCompleted) Color.White.copy(alpha = 0.4f) else neonCyan
                        )
                    }
                }
            }
        }
    }
}
