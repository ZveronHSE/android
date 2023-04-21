package ru.zveron.lots_feed.filters_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.selectors.RadioSelector
import ru.zveron.design.shimmering.shimmeringBackground
import ru.zveron.design.theme.gray3
import ru.zveron.design.theme.gray5
import ru.zveron.lots_feed.R
import ru.zveron.lots_feed.filters_screen.ui.categories.RootCategoriesUiState
import ru.zveron.lots_feed.filters_screen.ui.categories.RootCategoryUiState

@Composable
internal fun RootCategoriesSelector(
    rootCategoriesUiState: RootCategoriesUiState,
    modifier: Modifier = Modifier,
    onRootCategorySelected: (Int) -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 17.dp, vertical = 24.dp),
    ) {
        Text(
            text = stringResource(R.string.root_categories_selector_title),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = gray5,
            ),
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .selectableGroup()
                .fillMaxWidth(),
        ) {
            when (rootCategoriesUiState) {
                RootCategoriesUiState.Loading -> {
                    items(2) {
                        BoxWithConstraints(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .height(16.dp)
                                .width(120.dp)
                                .background(gray3)
                        ) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .shimmeringBackground(this.maxWidth)
                            )
                        }
                    }
                }

                is RootCategoriesUiState.Success -> {
                    items(
                        rootCategoriesUiState.categories,
                        key = { it.id }) { rootCategoryUiState ->
                        RootCategory(
                            rootCategoryUiState = rootCategoryUiState,
                            selectedId = rootCategoriesUiState.selectedCategoryId,
                            onRootCategorySelected = onRootCategorySelected,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RootCategory(
    rootCategoryUiState: RootCategoryUiState,
    selectedId: Int?,
    modifier: Modifier = Modifier,
    onRootCategorySelected: (Int) -> Unit = {},
) {
    val selectorClickHandler = remember(rootCategoryUiState, onRootCategorySelected) {
        { onRootCategorySelected.invoke(rootCategoryUiState.id) }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        RadioSelector(
            selected = rootCategoryUiState.id == selectedId,
            onClick = selectorClickHandler,
        )

        Text(
            text = rootCategoryUiState.name,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
            )
        )
    }
}