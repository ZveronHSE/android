package ru.zveron.favorites.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.gray5
import ru.zveron.favorites.R
import ru.zveron.favorites.ui.state.FavoritesCategoriesUiState
import ru.zveron.favorites.ui.state.FavoritesContentState
import ru.zveron.favorites.ui.state.FavoritesLotsUiState

@Composable
fun Favorites(
    categoryState: FavoritesCategoriesUiState,
    contentState: FavoritesContentState,
    modifier: Modifier = Modifier,
    onCategoryTabClicked: (Int) -> Unit = {},
    onRetryClicked: () -> Unit = {},
    onLotClick: (Long) -> Unit = {},
    onLotLikeClick: (Long) -> Unit = {},
) {
    val searchFilter = remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.favorites_screen_title),
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 28.sp,
                lineHeight = 34.sp,
                letterSpacing = 0.36.sp,
                color = gray5,
            ),
            modifier = Modifier.padding(start = 16.dp, top = 32.dp),
        )

        Spacer(Modifier.height(32.dp))

        SelectorRow(
            categoriesUiState = categoryState,
            onCategoryClick = onCategoryTabClicked,
        )

        Spacer(Modifier.height(16.dp))

        when(contentState) {
            is FavoritesLotsUiState -> FavoriteLotsContent(
                lotsState = contentState,
                searchFilter,
                onRetryClicked = onRetryClicked,
                onLotClick = onLotClick,
                onLotLikeClick = onLotLikeClick,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun FavoritesReadyPreview() {
    ZveronTheme {
        val categoryState = FavoritesCategoriesUiState.Loading

        val contentState = FavoritesLotsUiState.Loading
        
        Favorites(
            categoryState = categoryState,
            contentState = contentState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}