package ru.zveron.lots_feed.categories.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.R
import ru.zveron.design.fonts.Rubik
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.shimmering.shimmeringBackground
import ru.zveron.design.theme.ZveronTheme

@Composable
fun CategoryStub(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .shimmeringBackground(this.maxWidth)
            )
        }

        Spacer(Modifier.height(8.dp))
        
        BoxWithConstraints(
            modifier = Modifier
                .width(90.dp)
                .height(20.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .shimmeringBackground(this.maxWidth)
            )
        }
    }
}

@Composable
fun Category(
    category: CategoryUiState,
    modifier: Modifier = Modifier,
    onCategoryClick: (CategoryUiState) -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(
            onClickLabel = category.title,
            onClick = { onCategoryClick.invoke(category) },
        ),
    ) {
        ZveronImage(
            zveronImage = category.image,
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(CircleShape)
        )

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
        val category = CategoryUiState(
            id = 0,
            image = ZveronImage.ResourceImage(R.drawable.cool_dog),
            title = "Товары"
        )
        Category(category)
    }
}

@Preview
@Composable
internal fun CategoryStubPreview() {
    ZveronTheme {
        CategoryStub()
    }
}