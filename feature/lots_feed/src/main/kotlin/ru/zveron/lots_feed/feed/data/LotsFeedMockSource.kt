package ru.zveron.lots_feed.feed.data

import kotlinx.coroutines.delay
import ru.zveron.lots_feed.models.filters.Filter
import ru.zveron.lots_feed.models.sort.SortType
import ru.zveron.lots_feed.models.waterfall.Lot

class LotsFeedMockSource: LotsFeedSource {
    override suspend fun loadLots(
        filters: List<Filter>,
        sortType: SortType,
        pageSize: Int
    ): List<Lot> {
        delay(1000L)

        return listOf(
            Lot(
                id = 1,
                title = "Lot 1",
                price = "1$",
                publicationDate = "20.20.20",
                photoId = 0,
                isFavorite = false,
            ),
            Lot(
                id = 2,
                title = "Lot 2",
                price = "300$",
                publicationDate = "20.20.20",
                photoId = 0,
                isFavorite = false,
            ),
            Lot(
                id = 3,
                title = "Lot 3",
                price = "100500$",
                publicationDate = "20.20.20",
                photoId = 0,
                isFavorite = false,
            )
        )
    }
}