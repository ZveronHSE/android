package ru.zveron.lots_feed.feed.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.zveron.lots_feed.categories.domain.PassDataToFiltersInteractor
import ru.zveron.lots_feed.feed.LotsFeedNavigator
import ru.zveron.lots_feed.feed.data.LotsFeedRepository
import ru.zveron.lots_feed.mappings.toSortType
import ru.zveron.lots_feed.mappings.toUiLot
import ru.zveron.lots_feed.models.filters.Filter

internal class LotsFeedViewModel(
    private val lotsFeedRepository: LotsFeedRepository,
    private val passDataToFiltersInteractor: PassDataToFiltersInteractor,
    private val lotsFeedNavigator: LotsFeedNavigator,
): ViewModel() {
    private val _feedUiState = MutableStateFlow<LotsFeedUiState>(LotsFeedUiState.Loading)
    val feedUiState = _feedUiState.asStateFlow()

    private val _currentSortType = MutableStateFlow(SortType.DATE)
    val currentSortType = _currentSortType.asStateFlow()

    private val currentFilters = MutableStateFlow(listOf<Filter>())

    init {
        loadLots()
    }

    private fun loadLots() {
        viewModelScope.launch {
            try {
                _feedUiState.value = LotsFeedUiState.Loading
                val lotsResponse = lotsFeedRepository.loadLots(
                    currentFilters.value,
                    _currentSortType.value.toSortType(),
                )
                val newLots = lotsResponse.map { it.toUiLot() }

                _feedUiState.value = LotsFeedUiState.Success(newLots)
            } catch (e: Exception) {
                Log.e("Lots", "Error loading main page lots", e)
            }
        }
    }

    fun goToFilters() {
        passDataToFiltersInteractor.passDataToFilters()
        lotsFeedNavigator.goToFilters()
    }
}