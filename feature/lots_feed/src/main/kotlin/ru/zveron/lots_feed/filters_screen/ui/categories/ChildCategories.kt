package ru.zveron.lots_feed.filters_screen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.R
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.filters_screen.ui.categories.ChildrenCategoriesUiState

@Composable
internal fun ChildCategory(
    childCategoryUiState: ChildrenCategoriesUiState.Show,
    modifier: Modifier = Modifier,
    onChildCategoryClicked: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .height(48.dp)
            .clickable(
                onClickLabel = childCategoryUiState.selectedChildCategoryTitle.getText(),
                onClick = onChildCategoryClicked,
            )
            .padding(horizontal = 17.dp)
    ) {
        ZveronText(
            text = childCategoryUiState.selectedChildCategoryTitle,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            ),
        )

        Spacer(Modifier.weight(1f))

        Icon(
            painter = painterResource(R.drawable.ic_next),
            contentDescription = null,
        )
    }
}