package ru.zveron.lot_card.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.components.ActionButton
import ru.zveron.design.components.Stars
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.blackWithAlpha05
import ru.zveron.design.theme.enabledButtonGradient
import ru.zveron.design.theme.gray1
import ru.zveron.design.theme.gray5
import ru.zveron.lot_card.domain.Gender
import ru.zveron.lots_card.R
import ru.zveron.design.R as DesignR

@Composable
internal fun LotCard(
    state: LotCardUiState,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onActionClick: (CommunicationAction) -> Unit = {},
    onSellerClick: (Long) -> Unit = {},
) {
    when (state) {
        LotCardUiState.Loading -> LotCardLoading(modifier, onBackClicked)
        is LotCardUiState.Success -> LotCardSuccess(
            state,
            modifier,
            onBackClicked,
            onActionClick,
            onSellerClick,
        )
    }
}

@Composable
private fun LotCardLoading(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LotCardTopBar(onBackClicked = onBackClicked)

        Spacer(Modifier.weight(1f))

        CircularProgressIndicator()

        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun LotCardSuccess(
    state: LotCardUiState.Success,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onActionClick: (CommunicationAction) -> Unit = {},
    onSellerClick: (Long) -> Unit = {},
) {
    Column(modifier = modifier.background(MaterialTheme.colors.surface)) {
        LotCardContent(
            state = state,
            modifier = Modifier.weight(1f),
            onBackClicked = onBackClicked,
            onSellerClick = onSellerClick,
        )

        LotCardBottomButtons(
            price = state.price,
            onActionClick = onActionClick,
            communicationButtons = state.communicationButtons,
        )
    }
}

@Composable
internal fun LotCardContent(
    state: LotCardUiState.Success,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onSellerClick: (Long) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colors.surface)
            .padding(top = 16.dp),
    ) {
        item {
            LotCardTopBar(onBackClicked = onBackClicked)

            Spacer(Modifier.height(26.dp))
        }

        item {
            LotPhotoPager(state.photos)

            Spacer(Modifier.height(24.dp))
        }

        val isMale = when (state.gender) {
            Gender.MALE -> true
            Gender.FEMALE -> false
            Gender.METIS, Gender.UNKNOWN -> null
        }

        item {
            LotCardTitle(
                title = state.title,
                isMale = isMale,
                address = state.address,
            )

            Spacer(Modifier.height(16.dp))
        }

        item {
            LotCardStatistics(
                likes = state.favorites,
                views = state.views,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(Modifier.height(16.dp))
        }

        item {
            LotCardTags(state.tags)

            Spacer(Modifier.height(24.dp))
        }

        item {
            LotCardDescription(state.description)

            Spacer(Modifier.height(24.dp))
        }

        item {
            LotCardSeller(
                avatarImage = state.sellerAvatar,
                sellerName = state.sellerName,
                rating = state.rating,
                maxRating = state.maxRating,
                modifier = Modifier.padding(horizontal = 16.dp),
                onAuthorClick = {
                    onSellerClick.invoke(state.sellerId)
                }
            )

            Spacer(Modifier.height(38.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LotPhotoPager(
    photos: List<ZveronImage>,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState()

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            pageCount = photos.size,
        ) {
            ZveronImage(
                zveronImage = photos[it],
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(32.dp)),
                loadingImageModifier = Modifier.background(Color.LightGray),
            )
        }

        if (photos.size > 1) {
            LotCardPagerIndicator(
                count = photos.size,
                pagerState = pagerState,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.BottomCenter),
            )
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LotCardPagerIndicator(
    count: Int,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .height(50.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(count) { iteration ->
            key(iteration) {
                val isSelected by remember(pagerState.currentPage) {
                    mutableStateOf(pagerState.currentPage == iteration)
                }

                val color = if (isSelected) MaterialTheme.colors.primary else Color.White

                val size = if (isSelected) 20.dp else 10.dp

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color)
                        .size(size)

                )
            }
        }
    }
}

@Composable
private fun LotCardTopBar(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Icon(
            painterResource(DesignR.drawable.ic_back_triangle),
            stringResource(R.string.back_content_description),
            modifier = Modifier.clickable(
                enabled = true,
                onClickLabel = stringResource(R.string.back_content_description),
                role = Role.Button,
                onClick = onBackClicked,
            )
        )

        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun LotCardTitle(
    title: String,
    isMale: Boolean?,
    address: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = gray5,
                ),
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = address,
                style = TextStyle(
                    fontSize = 14.sp,
                    letterSpacing = (-0.24).sp,
                    fontWeight = FontWeight.Light,
                    color = gray5,
                ),
            )
        }

        isMale?.let {
            Spacer(Modifier.width(8.dp))

            val contentDescription = if (isMale) {
                stringResource(R.string.male_content_description)
            } else {
                stringResource(R.string.female_content_description)
            }

            val selectedModifier = Modifier
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                }
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(enabledButtonGradient, blendMode = BlendMode.SrcAtop)
                    }
                }

            val maleModifier = if (isMale) selectedModifier else Modifier
            val femaleModifier = if (!isMale) selectedModifier else Modifier

            Row(
                modifier = Modifier.semantics(mergeDescendants = true) {
                    stateDescription = contentDescription
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_male),
                    contentDescription = stringResource(R.string.male_content_description),
                    modifier = maleModifier.size(24.dp),
                )

                Spacer(Modifier.width(8.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_female),
                    contentDescription = stringResource(R.string.female_content_description),
                    modifier = femaleModifier.size(24.dp),
                )
            }
        }
    }
}

@Composable
private fun LotCardStatistics(
    likes: Int,
    views: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            painterResource(DesignR.drawable.heart_unliked),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Unspecified,
        )

        Text(
            text = likes.toString(),
            style = TextStyle(
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
            ),
        )

        Spacer(Modifier.width(8.dp))

        Icon(
            painterResource(DesignR.drawable.ic_eye),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Unspecified,
        )

        Text(
            text = views.toString(),
            style = TextStyle(
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
            ),
        )
    }
}

@Composable
private fun LotCardTags(
    tags: List<LotCardTag>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item { Spacer(Modifier.width(8.dp)) }

        items(tags) {
            Tag(
                it.title,
                it.subtitle,
            )
        }

        item { Spacer(Modifier.width(8.dp)) }
    }
}

@Composable
private fun LotCardDescription(
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        Text(
            text = stringResource(R.string.lot_card_description_title),
            style = TextStyle(
                color = blackWithAlpha05,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                letterSpacing = 0.sp,
            ),
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = description,
            style = TextStyle(
                color = gray5,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
                letterSpacing = 0.sp,
            ),
        )
    }
}

@Composable
private fun LotCardSeller(
    avatarImage: ZveronImage,
    sellerName: String,
    rating: Int,
    maxRating: Int,
    modifier: Modifier = Modifier,
    onAuthorClick: () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(gray1)
            .padding(16.dp)
            .clickable(onClick = onAuthorClick),
    ) {
        ZveronImage(
            zveronImage = avatarImage,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = sellerName,
                style = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    color = Color.Black,
                )
            )

            Stars(currentStars = rating, maxStars = maxRating)
        }
    }
}

@Composable
private fun LotCardBottomButtons(
    price: String,
    communicationButtons: List<CommunicationButton>,
    modifier: Modifier = Modifier,
    onActionClick: (CommunicationAction) -> Unit = {},
) {
    Card(
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = modifier.fillMaxWidth(),
        elevation = 16.dp,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            Text(
                text = price,
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    color = gray5,
                ),
            )

            if (communicationButtons.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    communicationButtons.forEach {
                        key(it.text) {
                            val clicker = remember {
                                { onActionClick.invoke(it.action) }
                            }

                            ActionButton(
                                modifier = Modifier.weight(1f),
                                onClick = clicker,
                                brush = it.brush,
                            ) {
                                ZveronText(
                                    text = it.text,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 16.sp,
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LotCardPreview() {
    val state = LotCardUiState.Success(
        photos = List(4) { ZveronImage.ResourceImage(DesignR.drawable.cool_dog) },
        title = "Продам собаку породы Немецкая овчарка",
        address = "Москва, Калининский р-он",
        tags = listOf(
            LotCardTag("Порода", "Английский бульдог"),
            LotCardTag("Возраст", "10 мес"),
            LotCardTag("Окрас", "Бежевый"),
        ),
        description = "С другой стороны, перспективное планирование предопределяет высокую востребованность своевременного выполнения сверхзадачи. Банальные, но неопровержимые выводы, а также стремящиеся вытеснить...",
        sellerId = 1,
        sellerAvatar = ZveronImage.ResourceImage(DesignR.drawable.person_avatar),
        sellerName = "Гигачад",
        rating = 4,
        maxRating = 5,
        price = "20 000 ₽",
        communicationButtons = emptyList(),
        views = 0,
        favorites = 0,
        gender = Gender.MALE,
    )

    ZveronTheme {
        LotCardSuccess(state, Modifier.fillMaxSize())
    }
}