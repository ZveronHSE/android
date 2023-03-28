package ru.zveron.lots_feed.feed.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.fonts.Rubik
import ru.zveron.design.lots.SearchBar
import ru.zveron.design.lots.LoadingLotCard
import ru.zveron.design.lots.LotCard
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.lots_feed.R
import ru.zveron.design.R as DesignR
import ru.zveron.lots_feed.categories.ui.Categories
import ru.zveron.lots_feed.categories.ui.CategoriesUiState
import ru.zveron.lots_feed.categories.ui.CategoryUiState
import ru.zveron.lots_feed.feed.ui.parameters.ParametersRow
import ru.zveron.lots_feed.feed.ui.parameters.ParametersUiState

private const val LOADING_STUBS_COUNT = 12
private const val BOTTOM_BAR_HEIGHT = 98

private fun LazyGridScope.fullWidthItem(
    key: Any? = null,
    contentType: Any? = null,
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(
        key = key,
        span = { GridItemSpan(3) },
        contentType = contentType,
        content = content,
    )
}

@Composable
internal fun LotsFeed(
    categoryTitle: ZveronText,
    feedUiState: LotsFeedUiState,
    categoriesUiState: CategoriesUiState,
    parametersUiState: ParametersUiState,
    currentSortType: SortType,
    modifier: Modifier = Modifier,
    onSearchClicked: () -> Unit = {},
    onFiltersClicked: () -> Unit = {},
    hasBackButton: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onSortTypeSelected: (SortType) -> Unit = {},
    onCategoryClick: (CategoryUiState) -> Unit = {},
    onParameterClick: (Int) -> Unit = {},
    onLotLikeClick: (Long) -> Unit = {},
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        fullWidthItem {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (hasBackButton) {
                    Icon(
                        painterResource(R.drawable.ic_back_triangle),
                        contentDescription = stringResource(R.string.back_hint),
                        modifier = Modifier.clickable(
                            role = Role.Button,
                            onClickLabel = stringResource(R.string.back_hint),
                            onClick = onNavigateBack,
                        )
                    )

                    Spacer(Modifier.width(8.dp))
                }

                val (query, setQuery) = remember {
                    mutableStateOf("")
                }

                SearchBar(
                    value = query,
                    onValueChange = setQuery,
                    inputHint = stringResource(R.string.search_input_hint),
                    alwaysKeepTrail = true,
                    trailFrame = {
                        Icon(
                            painter = painterResource(DesignR.drawable.ic_filter),
                            contentDescription = stringResource(R.string.filter_content_description),
                            tint = Color.Unspecified,
                            modifier = Modifier.clickable(
                                onClickLabel = stringResource(R.string.filter_content_description),
                                role = Role.Button,
                                onClick = onFiltersClicked,
                            ),
                        )
                    }
                )
            }
        }

        fullWidthItem { Spacer(Modifier.height(20.dp)) }

        fullWidthItem {
            ZveronText(
                categoryTitle,
                style = TextStyle(
                    fontFamily = Rubik,
                    fontWeight = FontWeight.Medium,
                    fontSize = 28.sp,
                    lineHeight = 33.18.sp,
                )
            )
        }

        if (categoriesUiState !is CategoriesUiState.Hidden) {
            fullWidthItem { Spacer(Modifier.height(24.dp)) }

            fullWidthItem {
                Categories(
                    categoriesUiState = categoriesUiState,
                    onCategoryClick = onCategoryClick,
                )
            }
        }

        if (parametersUiState !is ParametersUiState.Hidden) {
            fullWidthItem { Spacer(Modifier.height(6.dp)) }

            fullWidthItem {
                ParametersRow(
                    uiState = parametersUiState,
                    onParameterClick = onParameterClick,
                )
            }
        }

        fullWidthItem { Spacer(Modifier.height(32.dp)) }

        fullWidthItem {
            Box(
                Modifier.fillMaxWidth()
            ) {
                SortDropdown(
                    selectedSortType = currentSortType,
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onSortTypeSelected
                )
            }
        }

        fullWidthItem { Spacer(Modifier.height(16.dp)) }

        when (feedUiState) {
            LotsFeedUiState.Loading -> LoadingLots()
            is LotsFeedUiState.Success -> LotsGrid(
                feedUiState.lots,
                onLotLikeClick,
            )
        }
    }
}

private fun LazyGridScope.LoadingLots() {
    items(LOADING_STUBS_COUNT) {
        LoadingLotCard()
    }
}

private fun LazyGridScope.LotsGrid(
    items: List<LotUiState>,
    onLotLikeClick: (Long) -> Unit,
) {
    items(items, key = { it.id }) { lotUiState ->
        val clicker = remember {
            { onLotLikeClick.invoke(lotUiState.id) }
        }

        LotCard(
            zveronImage = lotUiState.image,
            title = lotUiState.title,
            price = lotUiState.price,
            date = lotUiState.date,
            isLiked = lotUiState.isLiked.value,
            onLikeClick = clicker,
        )
    }

    fullWidthItem { Spacer(Modifier.height(BOTTOM_BAR_HEIGHT.dp)) }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
private fun LotsFeedLoadingPreview() {
    ZveronTheme {
        LotsFeed(
            categoryTitle = ZveronText.RawString("Категории"),
            feedUiState = LotsFeedUiState.Loading,
            categoriesUiState = CategoriesUiState.Loading,
            parametersUiState = ParametersUiState.Hidden,
            currentSortType = SortType.EXPENSIVE,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
private fun LotsFeedSuccessPreview() {
    val feedState = LotsFeedUiState.Success(
        lots = listOf(
            LotUiState(
                id = 1,
                title = "Шапочка вязанная",
                price = "1$",
                date = "20.20.20",
                image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
                isLiked = remember { mutableStateOf(true) },
            ),
            LotUiState(
                id = 2,
                title = "Щапочка банан",
                price = "300$",
                date = "20.20.20",
                image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
                isLiked = remember { mutableStateOf(false) },

            ),
            LotUiState(
                id = 3,
                title = "Ошейник с брелком",
                price = "100500$",
                date = "20.20.20",
                image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
                isLiked = remember { mutableStateOf(true) },
            ),
            LotUiState(
                id = 4,
                title = "Ошейник зеленый",
                price = "100500$",
                date = "20.20.20",
                image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
                isLiked = remember { mutableStateOf(true) },
            ),
        )
    )

    val categoriesState = CategoriesUiState.Success(
        categories = listOf(
          CategoryUiState(
              id = 1,
              image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
              title = "Животные"
          ),
          CategoryUiState(
              id = 2,
              image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
              title = "Товары",
          )
        ),
    )

    val parametersUiState = ParametersUiState.Hidden

    val selectedSortType = SortType.DATE

    ZveronTheme {
        LotsFeed(
            categoryTitle = ZveronText.RawString("Категории"),
            categoriesUiState = categoriesState,
            feedUiState = feedState,
            parametersUiState = parametersUiState,
            currentSortType = selectedSortType,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
private fun LotsFeedLoadingWithBackPreview() {
    ZveronTheme {
        LotsFeed(
            categoryTitle = ZveronText.RawString("Категории"),
            feedUiState = LotsFeedUiState.Loading,
            categoriesUiState = CategoriesUiState.Loading,
            parametersUiState = ParametersUiState.Hidden,
            hasBackButton = true,
            currentSortType = SortType.DATE,
            modifier = Modifier.fillMaxSize(),
        )
    }
}