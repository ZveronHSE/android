package ru.zveron.favorites.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.zveron.design.components.LoadingChip
import ru.zveron.favorites.ui.state.FavoritesCategoriesUiState
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import ru.zveron.design.components.Chip
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.favorites.ui.state.CategoryUiState

@Composable
internal fun SelectorRow(
    categoriesUiState: FavoritesCategoriesUiState,
    modifier: Modifier = Modifier,
    onCategoryClick: (Int) -> Unit = {},
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.padding(start = 16.dp).fillMaxWidth(),
    ) {
        when (categoriesUiState) {
            FavoritesCategoriesUiState.Loading -> {
                items(3) {
                    LoadingChip()
                }
            }
            is FavoritesCategoriesUiState.Success -> {
                items(categoriesUiState.categories, key = { it.id }) { category ->
                    val clicker = remember {
                        { onCategoryClick.invoke(category.id) }
                    }

                    Chip(
                        title = ZveronText.RawString(category.title),
                        onClick = clicker,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun SelectorRowLoadingPreview() {
    ZveronTheme {
        val state = FavoritesCategoriesUiState.Loading

        SelectorRow(categoriesUiState = state)
    }
}


@Preview(showBackground = true)
@Composable
internal fun SelectorRowSuccessPreview() {
    ZveronTheme {
        val state = FavoritesCategoriesUiState.Success(
            listOf(
                CategoryUiState(
                    id = 1,
                    title = "Животные",
                ),
                CategoryUiState(
                    id = 2,
                    title = "Товары для животных"
                )
            )
        )

        SelectorRow(categoriesUiState = state)
    }
}
