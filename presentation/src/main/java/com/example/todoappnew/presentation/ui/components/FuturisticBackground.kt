package com.example.todoappnew.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.unit.dp

/**
 * A futuristic background featuring a deep purple/pink/navy gradient,
 * a blurred image overlay, and glassmorphism preparation.
 */
@Composable
fun FuturisticBackground(
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Base Gradient Layer (Navy -> Purple -> Pinkish)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F0C29), // Deep Navy
                            Color(0xFF302B63), // Purple
                            Color(0xFF24243E)  // Navy variant
                        )
                    )
                )
        )

        // 2. City Skyline / Futuristic Image Layer
        // Using a placeholder resource. In a production app, replace with a real skyline drawable.
        // We apply heavy blur and low alpha to create the 'futuristic mood' without distracting.
        /* 
        Image(
            painter = painterResource(id = com.example.todoappnew.R.drawable.city_skyline), 
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp)
                .alpha(0.15f),
            contentScale = ContentScale.Crop
        )
        */

        // 3. Neon Glow Overlays (Pink/Purple)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFF00E5).copy(alpha = 0.1f), // Neon Pink Glow
                            Color.Transparent
                        ),
                        radius = 1500f
                    )
                )
        )

        // 4. Subtle Grain/Overlay Layer for Glassmorphism depth
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
        )

        // Content on top
        content()
    }
}
