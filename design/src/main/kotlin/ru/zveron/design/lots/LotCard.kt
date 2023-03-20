package ru.zveron.design.lots

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.R
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.theme.ZveronTheme

@Composable
fun LotCard(
    zveronImage: ZveronImage,
    title: String,
    price: String,
    date: String,
    modifier: Modifier = Modifier,
    isLiked: Boolean = false,
    onLikeClick: () -> Unit = {},
    onCardClick: () -> Unit = {},
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        modifier = modifier.fillMaxWidth().clickable(onClick = onCardClick),
    ) {
        Column(modifier.fillMaxWidth()) {
            ZveronImage(
                zveronImage,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                loadingImageModifier = Modifier
                    .size(104.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.LightGray),
                readyImageModifier = Modifier.height(104.dp),
            )

            Row(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
            ) {
                Text(
                    title,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 14.84.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                    modifier = Modifier.width(120.dp),
                )

                Spacer(Modifier.weight(1f))

                val likeResource = if(isLiked) R.drawable.heart_liked else R.drawable.heart_unliked
                Icon(
                    painter = painterResource(id = likeResource),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.clickable(onClick = onLikeClick),
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                price,
                modifier = Modifier.padding(horizontal = 8.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 16.59.sp,
                    fontWeight = FontWeight.Medium,
                )
            )

            Spacer(Modifier.height(12.dp))

            Text(
                date,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                style = TextStyle(
                    fontSize = 10.sp,
                    lineHeight = 11.85.sp,
                    fontWeight = FontWeight.Light,
                )
            )
        }
    }
}

@Preview
@Composable
internal fun LotCardPreview() {
    ZveronTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SampleCard(modifier = Modifier.weight(1f))

            SampleCard(modifier = Modifier.weight(1f), isLiked = false)
        }
    }
}

@Preview
@Composable
internal fun LotCardGridPreview() {
    ZveronTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            for(i in 0..3) {
                item {
                    SampleCard(isLiked = i % 2 == 0)
                }
            }
        }
    }
}

@Composable
private fun SampleCard(modifier: Modifier = Modifier, isLiked: Boolean = true) {
    LotCard(
        zveronImage = ZveronImage.ResourceImage(R.drawable.cool_dog),
        title = "Продам щенков Корги. 2 месяца",
        price = "20 000 ₽",
        date = "сегодня",
        isLiked = isLiked,
        modifier = modifier,
    )
}