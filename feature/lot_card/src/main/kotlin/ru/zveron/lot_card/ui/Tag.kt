package ru.zveron.lot_card.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.theme.MainBackground
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.blackWithAlpha05
import ru.zveron.design.theme.gray5

@Composable
fun Tag(
    title: String,
    subtitle: String,
) {
    Card(
        backgroundColor = MainBackground,
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = title,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = blackWithAlpha05,
                ),
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = subtitle,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    letterSpacing = (-0.41).sp,
                    color = gray5,
                ),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF)
@Composable
private fun TagPreview() {
    ZveronTheme {
        Tag("Порода", "Английский бульдог")
    }
}