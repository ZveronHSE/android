package ru.zveron.design.lots

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.R
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.theme.ZveronTheme

/**
 * Does not support liking!!
 */
@Composable
fun LotRow(
    zveronImage: ZveronImage,
    title: String,
    price: String,
//    views: Int,
//    likes: Int,
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    onCardClick: () -> Unit = {},
) {
    val alpha = if (isActive) 1.0f else 0.5f

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .alpha(alpha)
            .clickable(onClick = onCardClick, enabled = isActive),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            ZveronImage(
                zveronImage,
                modifier = Modifier.fillMaxHeight().width(150.dp),
                contentScale = ContentScale.Crop,
                loadingImageModifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.LightGray),
            )

            Column(
                modifier = Modifier.padding(vertical = 8.dp),
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

                Text(
                    price,
                    style = TextStyle(
                        fontSize = 14.sp,
                        lineHeight = 16.59.sp,
                        fontWeight = FontWeight.Medium,
                    )
                )

//                Spacer(Modifier.weight(1f))

//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    Icon(
//                        painterResource(R.drawable.heart_unliked),
//                        contentDescription = null,
//                        modifier = Modifier.size(16.dp),
//                        tint = Color.Unspecified,
//                    )
//
//                    Text(
//                        text = likes.toString(),
//                        style = TextStyle(
//                            fontWeight = FontWeight.Light,
//                            fontSize = 12.sp,
//                        ),
//                    )
//
//                    Spacer(Modifier.width(8.dp))
//
//                    Icon(
//                        painterResource(R.drawable.ic_eye),
//                        contentDescription = null,
//                        modifier = Modifier.size(16.dp),
//                        tint = Color.Unspecified,
//                    )
//
//                    Text(
//                        text = views.toString(),
//                        style = TextStyle(
//                            fontWeight = FontWeight.Light,
//                            fontSize = 12.sp,
//                        ),
//                    )
//                }
            }
        }
    }
}

@Preview
@Composable
internal fun LotRowColumnPreview() {
    ZveronTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            for(i in 0..3) {
                LotRow(
                    zveronImage = ZveronImage.ResourceImage(R.drawable.cool_dog),
                    title = "Продам щенков Корги. 2 месяца",
                    price = "20 000 ₽",
//                    views = 100,
//                    likes = 100,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    isActive = i % 2 == 0,
                )
            }
        }
    }
}