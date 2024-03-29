package ru.zveron.favorites.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import ru.zveron.design.lots.LoadingLotCard
import ru.zveron.design.lots.LotCard
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.enabledButtonGradient
import ru.zveron.design.theme.gray3
import ru.zveron.favorites.R
import ru.zveron.favorites.ui.state.FavoritesLotsUiState
import ru.zveron.favorites.ui.state.LotUiState
import ru.zveron.design.R as DesignR

private const val BOTTOM_BAR_HEIGHT = 98

@Composable
internal fun ColumnScope.FavoriteLotsContent(
    lotsState: FavoritesLotsUiState,
    searchFilter: State<String>,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    refreshEnabled: Boolean = true,
    onRetryClicked: () -> Unit = {},
    onLotClick: (Long) -> Unit = {},
    onLotLikeClick: (Long) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    when (lotsState) {
        FavoritesLotsUiState.Error -> ErrorFavoriteLots(onRetryClicked)
        FavoritesLotsUiState.Loading -> LoadingFavoriteLots(modifier)
        is FavoritesLotsUiState.Success -> SuccessFavoriteLots(
            lots = lotsState.lots,
            searchFilter = searchFilter,
            isRefreshing = isRefreshing,
            modifier = modifier,
            refreshEnabled = refreshEnabled,
            onLotLikeClick = onLotLikeClick,
            onLotClick = onLotClick,
            onRefresh = onRefresh,
        )
    }
}

@Composable
private fun ColumnScope.LoadingFavoriteLots(
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .weight(1f)
            .fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(6) {
            LoadingLotCard()
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun ColumnScope.ErrorFavoriteLots(
    onRetryClicked: () -> Unit = {},
) {
    Spacer(Modifier.weight(1f))

    Icon(
        painterResource(DesignR.drawable.ic_warning),
        contentDescription = null,
        modifier = Modifier.align(Alignment.CenterHorizontally),
    )

    Text(
        text = stringResource(R.string.favorites_error_title),
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
        text = stringResource(R.string.favorites_error_button_title),
        style = TextStyle(
            brush = enabledButtonGradient,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
        ),
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .clickable(
                role = Role.Button,
                onClickLabel = stringResource(R.string.favorites_error_button_title),
                onClick = onRetryClicked,
            ),
    )

    Spacer(Modifier.weight(2f))
}

@Composable
private fun ColumnScope.SuccessFavoriteLots(
    lots: List<LotUiState>,
    searchFilter: State<String>,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    refreshEnabled: Boolean = true,
    onLotLikeClick: (Long) -> Unit = {},
    onLotClick: (Long) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val notEmpty by remember(lots) {
        // TODO: wrap this with [derivedStateOf]
        mutableStateOf(lots.isNotEmpty())
    }

    val visibleItems by remember(lots) {
        derivedStateOf {
            lots.filter { it.title.contains(searchFilter.value, true) }
        }
    }

    val visibleNotEmpty by remember(visibleItems) {
        derivedStateOf {
            visibleItems.isNotEmpty()
        }
    }

    when {
        visibleNotEmpty && notEmpty -> LotsNotEmpty(
            lots = visibleItems,
            isRefreshing = isRefreshing,
            modifier = modifier,
            refreshEnabled = refreshEnabled,
            onLotClick = onLotClick,
            onLotLikeClick = onLotLikeClick,
            onRefresh = onRefresh,
        )
        !visibleNotEmpty && notEmpty -> LotsEmptyBySearch()
        else -> LotsEmpty(
            isRefreshing = isRefreshing,
            modifier = modifier,
            refreshEnabled = refreshEnabled,
            onRefresh = onRefresh,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ColumnScope.LotsNotEmpty(
    lots: List<LotUiState>,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    refreshEnabled: Boolean = true,
    onLotLikeClick: (Long) -> Unit = {},
    onLotClick: (Long) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh,
    )

    Box(
        modifier = modifier
            .weight(1f)
            .fillMaxWidth()
            .pullRefresh(state = pullRefreshState, enabled = refreshEnabled)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(lots, { it.id }) { lot ->
                val likeClicker = remember {
                    { onLotLikeClick.invoke(lot.id) }
                }

                val lotClicker = remember {
                    { onLotClick.invoke(lot.id) }
                }

                LotCard(
                    zveronImage = lot.image,
                    title = lot.title,
                    price = lot.price,
                    date = lot.date,
                    isLiked = lot.isLiked.value,
                    onLikeClick = likeClicker,
                    onCardClick = lotClicker,
                )
            }

            item(span = { GridItemSpan(3) }) {
                Spacer(Modifier.height(BOTTOM_BAR_HEIGHT.dp))
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colors.primary,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ColumnScope.LotsEmpty(
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
    refreshEnabled: Boolean = true,
    onRefresh: () -> Unit = {},
) {
    val scrollableState = rememberScrollState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh,
    )

    Box(
        modifier = modifier
            .weight(1f)
            .fillMaxWidth()
            .pullRefresh(state = pullRefreshState, enabled = refreshEnabled)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollableState)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.favorites_empty_title),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = gray3,
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.weight(2f))
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colors.primary,
        )
    }
}

@Composable
private fun ColumnScope.LotsEmptyBySearch() {
    Spacer(modifier = Modifier.weight(1f))

    Icon(
        painterResource(DesignR.drawable.ic_question),
        null,
        modifier = Modifier.align(Alignment.CenterHorizontally),
    )

    Spacer(Modifier.height(8.dp))

    Text(
        text = stringResource(R.string.favorites_empty_by_search_title),
        style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            color = gray3,
            textAlign = TextAlign.Center,
        ),
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(horizontal = 32.dp),
    )

    Spacer(modifier = Modifier.weight(2f))
}

@Preview(showBackground = true)
@Composable
private fun FavoriteLotsLoadingPreview() {
    val state = FavoritesLotsUiState.Loading
    val searchFilter = remember {
        mutableStateOf("")
    }

    ZveronTheme {
        Column(Modifier.fillMaxSize()) {
            FavoriteLotsContent(lotsState = state, searchFilter = searchFilter, isRefreshing = false)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun FavoriteLotsErrorPreview(
) {
    val state = FavoritesLotsUiState.Error
    val searchFilter = remember {
        mutableStateOf("")
    }

    ZveronTheme {
        Column(Modifier.fillMaxSize()) {
            FavoriteLotsContent(lotsState = state, searchFilter = searchFilter, isRefreshing = false,)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteLotsSuccessPreview() {
    val state = FavoritesLotsUiState.Success(
        listOf(
            LotUiState(
                id = 1,
                title = "Хороший мальчик ищет хозяина",
                price = "Бесплатно",
                date = "Сегодня",
                image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
                isLiked = remember { mutableStateOf(true) },
                isActive = true,
            ),
            LotUiState(
                id = 2,
                title = "Хороший мальчик ищет хозяина",
                price = "Бесплатно",
                date = "Сегодня",
                image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
                isLiked = remember { mutableStateOf(true) },
                isActive = true,
            ),
            LotUiState(
                id = 3,
                title = "Хороший мальчик ищет хозяина",
                price = "Бесплатно",
                date = "Сегодня",
                image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
                isLiked = remember { mutableStateOf(true) },
                isActive = true,
            ),
        )
    )
    val searchFilter = remember {
        mutableStateOf("")
    }

    ZveronTheme {
        Column(Modifier.fillMaxSize()) {
            FavoriteLotsContent(lotsState = state, searchFilter = searchFilter, isRefreshing = false,)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteLotsSuccessEmptyPreview() {
    val state = FavoritesLotsUiState.Success(
        listOf(
//            LotUiState(
//                id = 1,
//                title = "Хороший мальчик ищет хозяина",
//                price = "Бесплатно",
//                date = "Сегодня",
//                image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
//                isLiked = remember { mutableStateOf(true) },
//            ),
//            LotUiState(
//                id = 2,
//                title = "Хороший мальчик ищет хозяина",
//                price = "Бесплатно",
//                date = "Сегодня",
//                image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
//                isLiked = remember { mutableStateOf(true) },
//            ),
//            LotUiState(
//                id = 3,
//                title = "Хороший мальчик ищет хозяина",
//                price = "Бесплатно",
//                date = "Сегодня",
//                image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
//                isLiked = remember { mutableStateOf(true) },
//            ),
        )
    )
    val searchFilter = remember {
        mutableStateOf("")
    }

    ZveronTheme {
        Column(Modifier.fillMaxSize()) {
            FavoriteLotsContent(lotsState = state, searchFilter = searchFilter, isRefreshing = false)
        }
    }
}