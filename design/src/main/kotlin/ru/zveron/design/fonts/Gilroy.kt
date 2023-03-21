package ru.zveron.design.fonts

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import ru.zveron.design.R

val Gilroy = FontFamily(
    Font(R.font.gilroy_regular, weight = FontWeight.Normal),
    Font(R.font.gilroy_black, weight = FontWeight.Black),
    Font(R.font.gilroy_bold, weight = FontWeight.Bold),
    Font(R.font.gilroy_extra_bold, weight = FontWeight.ExtraBold),
    Font(R.font.gilroy_light, weight = FontWeight.Light),
    Font(R.font.gilroy_medium, weight = FontWeight.Medium),
    Font(R.font.gilroy_semi_bold, weight = FontWeight.SemiBold),
    Font(R.font.gilroy_thin, weight = FontWeight.Thin),

    Font(R.font.gilroy_regular_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.gilroy_black_italic, weight = FontWeight.Black, style = FontStyle.Italic),
    Font(R.font.gilroy_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.gilroy_extra_bold_italic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),
    Font(R.font.gilroy_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.gilroy_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.gilroy_semi_bold_italic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
    Font(R.font.gilroy_thin_italic, weight = FontWeight.Thin, style = FontStyle.Italic),
)