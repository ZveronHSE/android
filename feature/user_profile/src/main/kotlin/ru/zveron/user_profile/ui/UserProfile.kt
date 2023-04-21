package ru.zveron.user_profile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.components.Chip
import ru.zveron.design.components.Stars
import ru.zveron.design.lots.LotCard
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.enabledButtonGradient
import ru.zveron.design.theme.gray3
import ru.zveron.design.wrappers.ListWrapper
import ru.zveron.user_profile.R
import ru.zveron.design.R as DesignR

@Composable
internal fun UserProfile(
    uiState: UserProfileUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onLotClick: (Long) -> Unit = {},
    onLotLikeClick: (Long) -> Unit = {},
    onTabClick: (UserProfileTab) -> Unit = {},
    onReviewsClick: () -> Unit = {},
    onRetryClicked: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(DesignR.drawable.ic_back_triangle),
                contentDescription = null,
            )
        }

        when (uiState) {
            UserProfileUiState.Loading -> UserProfileLoading()
            is UserProfileUiState.Success -> UserProfileSuccess(
                photo = uiState.photo,
                displayedName = uiState.displayedName,
                address = uiState.address,
                rating = uiState.rating,
                reviewAmount = uiState.reviewAmount,
                currentLots = uiState.currentLots,
                currentTab = uiState.currentTab,
                onTabClick = onTabClick,
                onLotClick = onLotClick,
                onLotLikeClick = onLotLikeClick,
                onReviewsClick = onReviewsClick,
            )

            UserProfileUiState.Error -> LotCardError(onRetryClicked)
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun ColumnScope.LotCardError(
    onRetryClicked: () -> Unit = {},
) {
    Spacer(Modifier.weight(1f))

    Icon(
        painterResource(ru.zveron.design.R.drawable.ic_warning),
        contentDescription = null,
        modifier = Modifier.align(Alignment.CenterHorizontally),
    )

    Text(
        text = stringResource(R.string.user_profile_error_title),
        style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = gray3,
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier.align(Alignment.CenterHorizontally),
    )

    Spacer(Modifier.height(24.dp))

    Text(
        text = stringResource(R.string.user_profile_error_retry_title),
        style = TextStyle(
            brush = enabledButtonGradient,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
        ),
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .clickable(
                role = Role.Button,
                onClickLabel = stringResource(R.string.user_profile_error_retry_title),
                onClick = onRetryClicked,
            ),
    )

    Spacer(Modifier.weight(2f))
}

@Composable
private fun ColumnScope.UserProfileLoading() {
    Spacer(Modifier.weight(1f))

    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))

    Spacer(Modifier.weight(1f))
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun ColumnScope.UserProfileSuccess(
    photo: ZveronImage,
    displayedName: ZveronText,
    address: String,
    rating: Double,
    reviewAmount: Int,
    currentLots: ListWrapper<LotUiState>,
    currentTab: UserProfileTab,
    onTabClick: (UserProfileTab) -> Unit = {},
    onLotClick: (Long) -> Unit = {},
    onLotLikeClick: (Long) -> Unit = {},
    onReviewsClick: () -> Unit = {},
) {
    Spacer(Modifier.height(32.dp))

    ZveronImage(
        zveronImage = photo,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(140.dp)
            .clip(CircleShape),
    )

    Spacer(Modifier.height(16.dp))

    ZveronText(
        text = displayedName,
        style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 20.sp,
            letterSpacing = (-0.5).sp,
            color = Color.Black,
        ),
        modifier = Modifier.align(Alignment.CenterHorizontally),
    )

    Spacer(Modifier.height(2.dp))

    Text(
        text = address,
        style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = (-0.5).sp,
        ),
        modifier = Modifier.align(Alignment.CenterHorizontally),
    )

    Spacer(Modifier.height(6.dp))

    Row(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Stars(currentStars = rating.toInt(), maxStars = 5)

        ZveronText(
            text = ZveronText.ArgResource(R.string.rating_format, rating.toString()),
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                letterSpacing = (-0.5).sp,
            ),
        )
    }

    ZveronText(
        text = ZveronText.PluralArgResource(
            R.plurals.review_format,
            reviewAmount,
            reviewAmount.toString(),
        ),
        style = TextStyle(
            enabledButtonGradient,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        ),
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .clickable(onClick = onReviewsClick),
    )

    Spacer(Modifier.height(32.dp))

    LazyRow(
        modifier = Modifier.padding(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(UserProfileTab.values(), key = { it }) { tab ->
            val isActive = tab == currentTab
            val clicker = remember {
                { onTabClick.invoke(tab) }
            }

            Chip(
                title = ZveronText.RawResource(tab.resInt),
                isActive = isActive,
                onClick = clicker,
            )
        }
    }

    Spacer(Modifier.height(16.dp))

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(currentLots.list, key = { it.id }) { lotUiState ->
            val likedClicker = remember {
                { onLotLikeClick.invoke(lotUiState.id) }
            }

            val cardClicker = remember {
                { onLotClick.invoke(lotUiState.id) }
            }

            LotCard(
                zveronImage = lotUiState.image,
                title = lotUiState.title,
                price = lotUiState.price,
                date = lotUiState.date,
                isLiked = lotUiState.isLiked.value,
                onLikeClick = likedClicker,
                onCardClick = cardClicker,
            )
        }
    }

}

@Composable
@Preview(showBackground = true, locale = "RU")
private fun UserProfileSuccessPreview() {
    ZveronTheme {
        val state = UserProfileUiState.Success(
            photo = ZveronImage.ResourceImage(DesignR.drawable.cool_dog),
            displayedName = ZveronText.RawString("Тест Тестович"),
            address = "село Нижние Подзалупки",
            rating = 4.0,
            reviewAmount = 2,
            currentTab = UserProfileTab.ACTIVE,
            currentLots = ListWrapper(
                listOf(
                    LotUiState(
                        id = 1,
                        title = "Шапочка вязанная",
                        price = "1$",
                        date = "20.20.20",
                        image = ZveronImage.ResourceImage(DesignR.drawable.cool_dog),
                        isLiked = remember { mutableStateOf(true) },
                    ),
                    LotUiState(
                        id = 2,
                        title = "Щапочка банан",
                        price = "300$",
                        date = "20.20.20",
                        image = ZveronImage.ResourceImage(DesignR.drawable.cool_dog),
                        isLiked = remember { mutableStateOf(false) },

                        ),
                    LotUiState(
                        id = 3,
                        title = "Ошейник с брелком",
                        price = "100500$",
                        date = "20.20.20",
                        image = ZveronImage.ResourceImage(DesignR.drawable.cool_dog),
                        isLiked = remember { mutableStateOf(true) },
                    ),
                    LotUiState(
                        id = 4,
                        title = "Ошейник зеленый",
                        price = "100500$",
                        date = "20.20.20",
                        image = ZveronImage.ResourceImage(DesignR.drawable.cool_dog),
                        isLiked = remember { mutableStateOf(true) },
                    ),
                )
            ),
        )

        UserProfile(uiState = state, modifier = Modifier.fillMaxSize())
    }
}

@Composable
@Preview(showBackground = true, locale = "RU")
private fun UserProfileLoadngPreview() {
    ZveronTheme {
        val state = UserProfileUiState.Loading

        UserProfile(uiState = state, modifier = Modifier.fillMaxSize())
    }
}

@Composable
@Preview(showBackground = true, locale = "RU")
private fun UserProfileErrorPreview() {
    ZveronTheme {
        val state = UserProfileUiState.Error

        UserProfile(uiState = state, modifier = Modifier.fillMaxSize())
    }
}