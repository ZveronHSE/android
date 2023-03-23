package ru.zveron.lots_feed.feed.data.feed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.zveron.categories.models.Category
import ru.zveron.lots_feed.filters_screen.data.parameters.ParameterState
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
        parameters: List<ParameterState>?,
        category: Category?,
        query: String?,
    ): List<Lot> {
        return withContext(Dispatchers.IO) {
            lotsFeedSource.loadLots(
                filters = filters,
                sortType = sortType,
                pageSize = PAGE_SIZE,
                parameters = parameters,
                category = category,
                query = query,
            )
        }
    }
}
