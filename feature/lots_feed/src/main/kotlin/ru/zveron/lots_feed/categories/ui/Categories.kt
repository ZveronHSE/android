package ru.zveron.lots_feed.categories.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.zveron.design.resources.LocalLoadingImageSize

private const val CATEGORIES_COUNT = 2

@Composable
internal fun Categories(
    categoriesUiState: CategoriesUiState,
    modifier: Modifier = Modifier,
    onCategoryClick: (CategoryUiState) -> Unit = {},
) {
    when (categoriesUiState) {
        CategoriesUiState.Loading -> LoadingCategories(modifier)
        is CategoriesUiState.Success -> ReadyCategories(
            categoriesUiState.categories,
            modifier,
            onCategoryClick
        )
        CategoriesUiState.Hidden -> {}
    }
}

@Composable
private fun LoadingCategories(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        repeat(CATEGORIES_COUNT) {
            CategoryStub()
        }
    }
}

@Composable
private fun ReadyCategories(
    categories: List<CategoryUiState>,
    modifier: Modifier = Modifier,
    onCategoryClick: (CategoryUiState) -> Unit = {},
) {
    CompositionLocalProvider(
        LocalLoadingImageSize provides 84.dp
    ) {
        if (categories.size <= CATEGORIES_COUNT) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.Top,
            ) {
                for (category in categories) {
                    key(category.id) {
                        Category(
                            category = category,
                            onCategoryClick = onCategoryClick,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        } else {
            LazyRow(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(categories, key = { it.id }) { category ->
                    Category(
                        category = category,
                        onCategoryClick = onCategoryClick,
                    )
                }
            }
        }
    }
}