package ru.zveron.lots_feed.filters_screen.data.sort_types

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.lots_feed.models.sort.SortType

class FiltersSelectedSortTypeRepository {
    private val _selectedSortType = MutableStateFlow<SortType>(SortType.Newest)
    val selectedSortType = _selectedSortType.asStateFlow()

    fun selectSortType(sortType: SortType) {
        _selectedSortType.update { sortType }
    }
}