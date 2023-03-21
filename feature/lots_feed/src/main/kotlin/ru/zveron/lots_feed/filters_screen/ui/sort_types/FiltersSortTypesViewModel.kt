package ru.zveron.lots_feed.filters_screen.ui.sort_types

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.zveron.lots_feed.feed.ui.SortType
import ru.zveron.lots_feed.filters_screen.data.sort_types.FiltersSelectedSortTypeRepository
import ru.zveron.lots_feed.mappings.toSortType
import ru.zveron.lots_feed.mappings.toUiSortType

internal class FiltersSortTypesViewModel(
    private val filtersSelectedSortTypeRepository: FiltersSelectedSortTypeRepository,
): ViewModel() {
    private val items = SortType.values().toList()

    private val defaultUiState = SortTypesUiState(items, SortType.DATE)

    val uiState = filtersSelectedSortTypeRepository.selectedSortType.map {
        SortTypesUiState(
            items,
            it.toUiSortType()
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, defaultUiState)

    fun sortTypeSelected(sortType: SortType) {
        filtersSelectedSortTypeRepository.selectSortType(sortType.toSortType())
    }
}