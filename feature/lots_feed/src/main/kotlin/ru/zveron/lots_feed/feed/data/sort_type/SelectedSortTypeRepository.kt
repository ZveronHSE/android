package ru.zveron.lots_feed.feed.data.sort_type

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.lots_feed.models.sort.SortType

class SelectedSortTypeRepository {
    private val _currentSortType = MutableStateFlow<SortType>(SortType.Newest)
    val currentSortType = _currentSortType.asStateFlow()

    fun selectSortType(sortType: SortType) {
        _currentSortType.update { sortType }
    }
}