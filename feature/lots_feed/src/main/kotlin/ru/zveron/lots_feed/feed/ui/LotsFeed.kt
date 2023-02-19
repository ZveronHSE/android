package ru.zveron.lots_feed.feed.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zveron.design.lots.LotCard
import ru.zveron.design.lots.SearchBar
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.shimmering.shimmeringBackground
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.lots_feed.R
import ru.zveron.lots_feed.categories.ui.Categories
import ru.zveron.lots_feed.categories.ui.CategoriesUiState
import ru.zveron.lots_feed.categories.ui.CategoryUiState

private const val LOADING_STUBS_COUNT = 12

@Composable
internal fun LotsFeed(
    feedUiState: LotsFeedUiState,
    categoriesUiState: CategoriesUiState,
    modifier: Modifier = Modifier,
    onSearchClicked: () -> Unit = {},
    onFiltersClicked: () -> Unit = {},
    hasBackButton: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onSortTypeSelected: (SortType) -> Unit = {},
    onCategoryClick: (Int) -> Unit = {},
) {
    Column(modifier = modifier.padding(top = 18.dp)) {
        SearchBar(
            searchTitle = stringResource(R.string.search_input_hint),
            filterContentDescription = stringResource(R.string.filter_content_description),
            onSearchClick = onSearchClicked,
            onOptions = onFiltersClicked,
            modifier = Modifier.padding(horizontal = 16.dp),
        )

        Spacer(Modifier.height(32.dp))

        Categories(
            categoriesUiState = categoriesUiState,
            modifier = Modifier.padding(horizontal = 16.dp),
            onCategoryClick = onCategoryClick,
        )

        Spacer(Modifier.height(32.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)) {
            SortDropdown(modifier = Modifier.align(Alignment.CenterEnd), onSortTypeSelected)
        }

        Spacer(Modifier.height(16.dp))

        when (feedUiState) {
            LotsFeedUiState.Loading -> LoadingLots()
            is LotsFeedUiState.Success -> LotsGrid(feedUiState.lots)
        }
    }
}

@Composable
private fun LoadingLots(
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(LOADING_STUBS_COUNT) {
            BoxWithConstraints(
                Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF717171))
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .shimmeringBackground(maxWidth))
            }
        }
    }
}

@Composable
private fun LotsGrid(
    items: List<LotUiState>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items, key = { it.id }) { lotUiState ->
            LotCard(
                zveronImage = lotUiState.image,
                title = lotUiState.title,
                price = lotUiState.price,
                date = lotUiState.date,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
private fun LotsFeedLoadingPreview() {
    ZveronTheme {
        LotsFeed(
            feedUiState = LotsFeedUiState.Loading,
            categoriesUiState = CategoriesUiState.Loading,
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
                title = "Lot 1",
                price = "1$",
                date = "20.20.20",
                image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
            ),
            LotUiState(
                id = 2,
                title = "Lot 2",
                price = "300$",
                date = "20.20.20",
                image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
            ),
            LotUiState(
                id = 3,
                title = "Lot 3",
                price = "100500$",
                date = "20.20.20",
                image = ZveronImage.ResourceImage(ru.zveron.design.R.drawable.cool_dog),
            )
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

    ZveronTheme {
        LotsFeed(
            categoriesUiState = categoriesState,
            feedUiState = feedState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}