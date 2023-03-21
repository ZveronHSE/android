package ru.zveron.design.fonts

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import ru.zveron.design.R

val Rubik = FontFamily(
    Font(R.font.rubik_black, weight = FontWeight.Black),
    Font(R.font.rubik_bold, weight = FontWeight.Bold),
    Font(R.font.rubik_extra_bold, weight = FontWeight.ExtraBold),
    Font(R.font.rubik_light, weight = FontWeight.Light),
    Font(R.font.rubik_medium, weight = FontWeight.Medium),
    Font(R.font.rubik_regular, weight = FontWeight.Normal),
    Font(R.font.rubik_semi_bold, weight = FontWeight.SemiBold),

    Font(R.font.rubik_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.rubik_black_italic, weight = FontWeight.Black, style = FontStyle.Italic),
    Font(R.font.rubik_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.rubik_extra_bold_italic, weight = FontWeight.ExtraBold, style = FontStyle.Italic),
    Font(R.font.rubik_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.rubik_mediumitalic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.rubik_semi_bold_italic, weight = FontWeight.SemiBold, style = FontStyle.Italic),
)