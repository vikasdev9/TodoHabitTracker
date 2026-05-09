package com.example.todoappnew.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun PremiumBackground(
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // High-Vibrancy Futuristic Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2E004E), // Very Deep Purple
                            Color(0xFF8E005B), // Magenta/Dark Pink
                            Color(0xFF0A1128)  // Midnight Navy
                        )
                    )
                )
        )

        // Dynamic Glow Spots for depth and "City Lights" feel
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF006E).copy(alpha = 0.15f), // Neon Pink Glow
                            Color.Transparent
                        ),
                        center = androidx.compose.ui.geometry.Offset(100f, 600f),
                        radius = 1200f
                    )
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF00FBFF).copy(alpha = 0.12f), // Cyan Glow
                            Color.Transparent
                        ),
                        center = androidx.compose.ui.geometry.Offset(1100f, 1800f),
                        radius = 1400f
                    )
                )
        )

        // Darkening overlay to ensure content pop
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f))
        )

        content()
    }
}
