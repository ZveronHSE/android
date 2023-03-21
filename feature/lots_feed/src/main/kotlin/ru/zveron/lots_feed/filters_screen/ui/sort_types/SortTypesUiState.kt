package ru.zveron.lots_feed.filters_screen.ui.sort_types

import ru.zveron.lots_feed.feed.ui.SortType

data class SortTypesUiState(
    val sortTypeItems: List<SortType>,
    val selectedSortType: SortType,
)
