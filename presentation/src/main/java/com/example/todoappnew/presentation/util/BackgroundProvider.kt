package com.example.todoappnew.presentation.util

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.todoappnew.domain.model.AppBackground

object BackgroundProvider {
    fun getBackgroundBrush(background: AppBackground): Brush {
        return when (background) {
            AppBackground.DEFAULT -> Brush.verticalGradient(listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB)))
            AppBackground.AURORA -> Brush.verticalGradient(listOf(Color(0xFF004D40), Color(0xFF00BFA5), Color(0xFF64FFDA)))
            AppBackground.SUNSET -> Brush.verticalGradient(listOf(Color(0xFFFF5722), Color(0xFFFF9800), Color(0xFFFFEB3B)))
            AppBackground.OCEAN -> Brush.verticalGradient(listOf(Color(0xFF01579B), Color(0xFF0288D1), Color(0xFF03A9F4)))
            AppBackground.MINT -> Brush.verticalGradient(listOf(Color(0xFFE8F5E9), Color(0xFFA5D6A7), Color(0xFF81C784)))
            AppBackground.PEACH -> Brush.verticalGradient(listOf(Color(0xFFFFF3E0), Color(0xFFFFCCBC), Color(0xFFFFAB91)))
            AppBackground.MIDNIGHT -> Brush.verticalGradient(listOf(Color(0xFF0D1117), Color(0xFF161B22), Color(0xFF21262D)))
            AppBackground.LAVENDER -> Brush.verticalGradient(listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7), Color(0xFFCE93D8)))
            AppBackground.SHANGHAI -> Brush.verticalGradient(listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2)))
            AppBackground.TOKYO -> Brush.verticalGradient(listOf(Color(0xFF240b36), Color(0xFFc31432)))
        }
    }

    fun getThumbnailColors(background: AppBackground): List<Color> {
        return when (background) {
            AppBackground.DEFAULT -> listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
            AppBackground.AURORA -> listOf(Color(0xFF004D40), Color(0xFF64FFDA))
            AppBackground.SUNSET -> listOf(Color(0xFFFF5722), Color(0xFFFFEB3B))
            AppBackground.OCEAN -> listOf(Color(0xFF01579B), Color(0xFF03A9F4))
            AppBackground.MINT -> listOf(Color(0xFFE8F5E9), Color(0xFF81C784))
            AppBackground.PEACH -> listOf(Color(0xFFFFF3E0), Color(0xFFFFAB91))
            AppBackground.MIDNIGHT -> listOf(Color(0xFF0D1117), Color(0xFF21262D))
            AppBackground.LAVENDER -> listOf(Color(0xFFF3E5F5), Color(0xFFCE93D8))
            AppBackground.SHANGHAI -> listOf(Color(0xFF4A00E0), Color(0xFF8E2DE2))
            AppBackground.TOKYO -> listOf(Color(0xFF240b36), Color(0xFFc31432))
        }
    }
}
