package ru.zveron.lots_feed.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.fonts.Rubik
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.theme.ZveronTheme

@Immutable
data class Category(
    val image: ZveronImage,
    val title: String,
)

@Composable
fun Category(
    category: Category,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        ZveronImage(zveronImage = category.image)

        Spacer(Modifier.height(8.dp))

        Text(
            category.title,
            style = TextStyle(
                fontFamily = Rubik,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 18.96.sp,
            ),
        )
    }
}

@Preview
@Composable
internal fun CategoryPreview() {
    ZveronTheme {
        val category = Category(
            image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
            title = "Товары"
        )
        Category(category)
    }
}