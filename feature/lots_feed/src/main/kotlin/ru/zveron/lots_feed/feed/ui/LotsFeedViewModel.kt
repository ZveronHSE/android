package ru.zveron.lots_feed.feed.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zveron.authorization.domain.AuthorizationEventsEmitter
import ru.zveron.authorization.storage.AuthorizationStorage
import ru.zveron.lots_feed.categories.domain.PassDataToFiltersInteractor
import ru.zveron.lots_feed.feed.LotsFeedNavigator
import ru.zveron.lots_feed.feed.domain.LikeLotInteractor
import ru.zveron.lots_feed.feed.domain.LoadFeedInteractor
import ru.zveron.lots_feed.feed.domain.SelectSortTypeInteractor
import ru.zveron.lots_feed.feed.domain.UpdateFeedInteractor
import ru.zveron.lots_feed.mappings.toSortType
import ru.zveron.lots_feed.mappings.toUiLot
import ru.zveron.lots_feed.mappings.toUiSortType
import ru.zveron.lots_feed.models.filters.Filter

internal class LotsFeedViewModel(
    private val passDataToFiltersInteractor: PassDataToFiltersInteractor,
    private val lotsFeedNavigator: LotsFeedNavigator,
    updateFeedInteractor: UpdateFeedInteractor,
    private val selectedSortTypeInteractor: SelectSortTypeInteractor,
    private val loadFeedInteractor: LoadFeedInteractor,
    private val likeLotInteractor: LikeLotInteractor,
    // TODO: replace with more generic interactor
    private val authorizationStorage: AuthorizationStorage,
    private val authorizationEventsEmitter: AuthorizationEventsEmitter,
) : ViewModel() {
    private val _feedUiState = MutableStateFlow<LotsFeedUiState>(LotsFeedUiState.Loading)
    val feedUiState = _feedUiState.asStateFlow()

    val currentSortType = selectedSortTypeInteractor.selectedSortType
        .map { it.toUiSortType() }
        .stateIn(viewModelScope, SharingStarted.Lazily, SortType.DATE)

    private val currentFilters = MutableStateFlow(listOf<Filter>())

    init {
        updateFeedInteractor.update()

        updateFeedInteractor.updateFlow
            .onEach { loadLots() }
            .launchIn(viewModelScope)
    }

    private fun loadLots() {
        viewModelScope.launch {
            try {
                _feedUiState.value = LotsFeedUiState.Loading
                val lotsResponse = loadFeedInteractor.loadLots(currentFilters.value)
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

    fun sortTypeSelected(sortType: SortType) {
        selectedSortTypeInteractor.selectSortType(sortType.toSortType())
    }

    fun lotLikedClicked(id: Long) {
        if (authorizationStorage.isAuthorized()) {
            likeLot(id)
        } else {
            lotsFeedNavigator.startAuthorization()
            viewModelScope.launch {
                val isSuccessfulAuthorization = authorizationEventsEmitter.waitForAuthorization()
                if (!isSuccessfulAuthorization) {
                    return@launch
                }
                likeLot(id)
            }
        }
    }

    private fun likeLot(id: Long) {
        val lot = findLot(id) ?: return
        val desiredFavoriteStatus = !lot.isLiked.value
        lot.isLiked.value = desiredFavoriteStatus

        viewModelScope.launch {
            try {
                likeLotInteractor.setLotFavoriteStatus(id, desiredFavoriteStatus)
                Log.d("Favorites", "Liked $id lot with status $desiredFavoriteStatus")
            } catch (e: Exception) {
                Log.e("Favorites", "Error liking $id lot with status $desiredFavoriteStatus")
            }
        }
    }

    private fun findLot(id: Long): LotUiState? {
        return when (val currentState = feedUiState.value) {
            LotsFeedUiState.Loading -> null
            is LotsFeedUiState.Success -> currentState.lots.find { it.id == id }
        }
    }
}