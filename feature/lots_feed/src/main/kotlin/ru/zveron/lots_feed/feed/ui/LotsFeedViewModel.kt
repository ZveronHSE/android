package ru.zveron.lots_feed.feed.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.zveron.design.R
import ru.zveron.contract.lot.Filter
import ru.zveron.design.resources.ZveronImage
import ru.zveron.lots_feed.feed.data.LotsFeedRepository

class LotsFeedViewModel(
    private val lotsFeedRepository: LotsFeedRepository,
): ViewModel() {
    private val _feedUiState = MutableStateFlow<LotsFeedUiState>(LotsFeedUiState.Loading)
    val feedUiState = _feedUiState.asStateFlow()

    private val _currentSortType = MutableStateFlow(SortType.DATE)
    val currentSortType = _currentSortType.asStateFlow()

    private val currentFilters = MutableStateFlow(listOf<Filter>())

    fun loadLots() {
        viewModelScope.launch(Dispatchers.IO) {
            _feedUiState.value = LotsFeedUiState.Loading
            val lotsResponse = lotsFeedRepository.loadLots(currentFilters.value, _currentSortType.value)
            val newLots = lotsResponse.lotsList.map {
                LotUiState(
                    id = it.id,
                    title = it.title,
                    price = it.price,
                    date = it.publicationDate,
                    image = ZveronImage.ResourceImage(R.drawable.cool_dog)
                )
            }

            _feedUiState.value = LotsFeedUiState.Success(newLots)
        }
    }
}