package ru.zveron.lots_feed.feed.domain

import ru.zveron.lots_feed.feed.data.sort_type.SelectedSortTypeRepository
import ru.zveron.lots_feed.models.sort.SortType

class SelectSortTypeInteractor(
    private val selectedSortTypeRepository: SelectedSortTypeRepository,
    private val updateFeedInteractor: UpdateFeedInteractor,
) {
    val selectedSortType = selectedSortTypeRepository.currentSortType

    fun selectSortType(sortType: SortType) {
        selectedSortTypeRepository.selectSortType(sortType)
        updateFeedInteractor.update()
    }
}