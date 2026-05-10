package com.example.todoappnew.presentation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.todoappnew.domain.model.AppBackground
import com.example.todoappnew.presentation.util.BackgroundProvider

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PremiumBackground(
    background: AppBackground,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = background,
            transitionSpec = {
                fadeIn(animationSpec = tween(700)) togetherWith
                        fadeOut(animationSpec = tween(700))
            },
            label = "BackgroundTransition"
        ) { targetBg ->
            val imageRes = BackgroundProvider.getBackgroundImage(targetBg)
            
            Box(modifier = Modifier.fillMaxSize()) {
                if (imageRes != null) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackgroundProvider.getBackgroundBrush(targetBg))
                    )
                }

                // Darkening overlay to ensure content pop and readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.35f))
                )
            }
        }

        content()
    }
}
