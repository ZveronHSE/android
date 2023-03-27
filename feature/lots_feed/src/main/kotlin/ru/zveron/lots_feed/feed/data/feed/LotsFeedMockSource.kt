package ru.zveron.lots_feed.feed.data.feed

import kotlinx.coroutines.delay
import ru.zveron.categories.models.Category
import ru.zveron.lots_feed.filters_screen.data.parameters.ParameterState
import ru.zveron.lots_feed.models.filters.Filter
import ru.zveron.lots_feed.models.sort.SortType
import ru.zveron.models.lots.Lot
import ru.zveron.models.lots.Status

class LotsFeedMockSource: LotsFeedSource {
    override suspend fun loadLots(
        filters: List<Filter>,
        sortType: SortType,
        pageSize: Int,
        parameters: List<ParameterState>?,
        category: Category?,
        query: String?
    ): List<Lot> {
        delay(1000L)

        return List(12) {
            Lot(
                id = it.toLong(),
                title = "Lot 1",
                price = "1$",
                publicationDate = "20.20.20",
                photoUrl = "https://img.icons8.com/3d-fluency/256/dog.png",
                isFavorite = false,
                categoryId = null,
                status = Status.ACTIVE,
            )
        }
    }
}