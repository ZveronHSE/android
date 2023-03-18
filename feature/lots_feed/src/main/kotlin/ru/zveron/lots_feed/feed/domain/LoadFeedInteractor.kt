package ru.zveron.lots_feed.feed.domain

import ru.zveron.lots_feed.feed.data.feed.LotsFeedRepository
import ru.zveron.lots_feed.models.filters.Filter
import ru.zveron.lots_feed.models.waterfall.Lot

class LoadFeedInteractor(
    private val lotsFeedRepository: LotsFeedRepository,

    private val selectedSortTypeInteractor: SelectSortTypeInteractor,
) {
    suspend fun loadLots(
        filters: List<Filter>,
    ): List<Lot> {
        val sortType = selectedSortTypeInteractor.selectedSortType.value

        return lotsFeedRepository.loadLots(filters, sortType)
    }
}