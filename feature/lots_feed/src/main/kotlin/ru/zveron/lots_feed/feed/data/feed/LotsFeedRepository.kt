package ru.zveron.lots_feed.feed.data.feed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.zveron.lots_feed.models.filters.Filter
import ru.zveron.lots_feed.models.sort.SortType
import ru.zveron.lots_feed.models.waterfall.Lot

private const val PAGE_SIZE = 32

class LotsFeedRepository(
    private val lotsFeedSource: LotsFeedSource,
) {
    suspend fun loadLots(
        filters: List<Filter>,
        sortType: SortType,
    ): List<Lot> {
        return withContext(Dispatchers.IO) {
            lotsFeedSource.loadLots(filters, sortType, PAGE_SIZE)
        }
    }
}
