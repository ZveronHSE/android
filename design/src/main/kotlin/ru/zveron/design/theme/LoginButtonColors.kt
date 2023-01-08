package ru.zveron.design.theme

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import ru.zveron.design.R

interface LoginButtonColors {
    val foregroundColor: Color

    val backgroundColor: Color

    @get:DrawableRes val iconRes: Int
}

object VkColors: LoginButtonColors {
    override val foregroundColor: Color = Color.White

    override val backgroundColor: Color = Color(0xFF0077FF)

    override val iconRes: Int = R.drawable.ic_vk_icon
}

object GoogleColors: LoginButtonColors {
    override val foregroundColor: Color = Color.Unspecified

    override val backgroundColor: Color = Color.White

    override val iconRes: Int = R.drawable.ic_google_icon
}