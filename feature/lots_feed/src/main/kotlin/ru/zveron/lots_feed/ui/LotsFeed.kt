package ru.zveron.lots_feed.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zveron.authorization.R
import ru.zveron.design.lots.SearchBar
import ru.zveron.design.theme.ZveronTheme

@Composable
internal fun LotsFeed(
    modifier: Modifier = Modifier,
    onSearchClicked: () -> Unit = {},
    onFiltersClicked: () -> Unit = {},
    hasBackButton: Boolean = false,
    onNavigateBack: () -> Unit = {},
) {
    Column(modifier = modifier.padding(top = 18.dp)) {
        SearchBar(
            searchTitle = stringResource(R.string.search_input_hint),
            filterContentDescription = stringResource(R.string.filter_content_description),
            onSearchClick = onSearchClicked,
            onOptions = onFiltersClicked,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF9F9F9)
@Composable
private fun LotsFeedPreview() {
    ZveronTheme {
        LotsFeed(modifier = Modifier.fillMaxSize())
    }
}