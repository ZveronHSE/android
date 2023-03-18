package ru.zveron.lots_feed.feed.data.feed

import ru.zveron.lots_feed.models.filters.Filter
import ru.zveron.lots_feed.models.sort.SortType
import ru.zveron.lots_feed.models.waterfall.Lot

interface LotsFeedSource {
    suspend fun loadLots(
        filters: List<Filter>,
        sortType: SortType,
        pageSize: Int,
    ): List<Lot>
}