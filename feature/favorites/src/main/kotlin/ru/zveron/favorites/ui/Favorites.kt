package ru.zveron.favorites.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.fonts.Rubik
import ru.zveron.design.lots.EditableSearchBar
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.gray5
import ru.zveron.favorites.R
import ru.zveron.design.R as DesignR
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
    onDeleteAllClicked: () -> Unit = {},
    onDeleteUnactiveClicked: () -> Unit = {},
) {
    val searchFilter = remember {
        mutableStateOf("")
    }

    var deleteDropdownVisible by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
            horizontalArrangement = Arrangement.Center,
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
                modifier = Modifier.padding(start = 16.dp),
            )

            Spacer(Modifier.weight(1f))

            if (contentState is FavoritesLotsUiState.Success) {
                Box(
                    modifier = modifier.wrapContentSize(Alignment.Center)
                ) {
                    DeleteDropdown(
                        isDropdownVisible = deleteDropdownVisible,
                        onDropdownVisibleChanged = { deleteDropdownVisible = it },
                        onDeleteAllClicked = onDeleteAllClicked,
                        onDeleteUnactiveClicked = onDeleteUnactiveClicked,
                    )
                }
            }
        }

        if (contentState is FavoritesLotsUiState.Success && contentState.lots.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))

            EditableSearchBar(
                value = searchFilter.value,
                onValueChange = { searchFilter.value = it },
                inputHint = stringResource(R.string.favorites_search_hint)
            )
        }

        Spacer(Modifier.height(32.dp))

        SelectorRow(
            categoriesUiState = categoryState,
            onCategoryClick = onCategoryTabClicked,
        )

        Spacer(Modifier.height(16.dp))

        when (contentState) {
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
private fun DeleteDropdown(
    isDropdownVisible: Boolean,
    onDropdownVisibleChanged: (Boolean) -> Unit,
    onDeleteAllClicked: () -> Unit = {},
    onDeleteUnactiveClicked: () -> Unit = {},
) {
    IconButton(onClick = { onDropdownVisibleChanged(true) }) {
        Icon(
            painterResource(DesignR.drawable.ic_options),
            stringResource(R.string.favorites_delete_description),
        )
    }

    DropdownMenu(
        expanded = isDropdownVisible,
        onDismissRequest = { onDropdownVisibleChanged(false) },
    ) {
        DropdownMenuItem(onClick = {
            onDropdownVisibleChanged(false)
            onDeleteUnactiveClicked()
        }) {
            Text(
                text = stringResource(R.string.favorites_remove_unactive_title),
                style = TextStyle(
                    fontFamily = Rubik,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 18.96.sp,
                ),
            )
        }

        DropdownMenuItem(onClick = {
            onDropdownVisibleChanged(false)
            onDeleteAllClicked()
        }) {
            Text(
                text = stringResource(R.string.favorites_remove_all_title),
                style = TextStyle(
                    fontFamily = Rubik,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    lineHeight = 18.96.sp,
                ),
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