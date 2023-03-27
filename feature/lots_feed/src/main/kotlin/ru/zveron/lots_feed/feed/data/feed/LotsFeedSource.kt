package ru.zveron.lots_feed.feed.data.feed

import ru.zveron.categories.models.Category
import ru.zveron.lots_feed.filters_screen.data.parameters.ParameterState
import ru.zveron.lots_feed.models.filters.Filter
import ru.zveron.lots_feed.models.sort.SortType
import ru.zveron.models.lots.Lot

interface LotsFeedSource {
    suspend fun loadLots(
        filters: List<Filter>,
        sortType: SortType,
        pageSize: Int,
        parameters: List<ParameterState>?,
        category: Category?,
        query: String?,
    ): List<Lot>
}