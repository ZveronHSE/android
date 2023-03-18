package ru.zveron.lots_feed.feed.domain

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.zveron.lots_feed.feed.data.feed.LotsFeedRepository
import ru.zveron.lots_feed.models.filters.Filter
import ru.zveron.lots_feed.models.sort.SortType
import ru.zveron.lots_feed.models.waterfall.Lot

class UpdateFeedInteractor(
    private val lotsFeedRepository: LotsFeedRepository,
) {
    private val _updateFlow = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1)
    val updateFlow = _updateFlow.asSharedFlow()

    fun update() {
        _updateFlow.tryEmit(Unit)
    }

    suspend fun loadLots(
        filters: List<Filter>,
        sortType: SortType,
    ): List<Lot> {
        return lotsFeedRepository.loadLots(filters, sortType)
    }
}