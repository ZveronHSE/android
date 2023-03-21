package ru.zveron.lots_feed.filters_screen.ui.sort_types

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import ru.zveron.design.selectors.Selector
import ru.zveron.design.theme.blackWithAlpha02
import ru.zveron.lots_feed.R
import ru.zveron.lots_feed.feed.ui.SortType

@Composable
fun SortTypes(
    sortTypesUiState: SortTypesUiState,
    modifier: Modifier = Modifier,
    onSortTypeSelected: (SortType) -> Unit = {},
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(16.dp),
    ) {
        Text(
            text = stringResource(R.string.sort_type_title),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = blackWithAlpha02,
            ),
        )

        Spacer(Modifier.height(24.dp))

        sortTypesUiState.sortTypeItems.forEachIndexed { index, sortType ->
//            key(sortType) {
                val isSelected = sortType == sortTypesUiState.selectedSortType

                val clicker = remember {
                    { onSortTypeSelected.invoke(sortType) }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Selector(
                        selected = isSelected,
                        onClick = clicker,
                    )

                    Text(
                        text = stringResource(sortType.titleRes),
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    )
                }

                if (index != sortTypesUiState.sortTypeItems.lastIndex) {
                    Spacer(Modifier.height(24.dp))
                }
//            }
        }
    }
}