package ru.zveron.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

// TODO: support dark theme
//private val DarkColorScheme = darkColors(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//)

private val LightColorScheme = lightColors(
    primary = orangeGradientStart,
    secondary = orangeGradientEnd,
    background = GreyBackground,
    surface = WhiteSurfaceBackground,
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ZveronTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }

    MaterialTheme(
        colors = LightColorScheme,
        typography = Typography,
        content = content
    )
}