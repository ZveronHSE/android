package ru.zveron.design.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val startColor = Color(0xFFE28213)
private val endColor = Color(0xFFFFBC37)

val enabledButtonGradient = Brush.horizontalGradient(
    colors = listOf(startColor, endColor),
)