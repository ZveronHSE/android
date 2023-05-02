package ru.zveron.lots_feed.feed.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.authorization.domain.AuthorizationEventsEmitter
import ru.zveron.authorization.storage.AuthorizationStorage
import ru.zveron.lots_feed.categories.domain.PassDataToFiltersInteractor
import ru.zveron.lots_feed.feed.LotsFeedNavigator
import ru.zveron.lots_feed.feed.domain.LikeLotInteractor
import ru.zveron.lots_feed.feed.domain.LoadFeedInteractor
import ru.zveron.lots_feed.feed.domain.QueryInteractor
import ru.zveron.lots_feed.feed.domain.SelectSortTypeInteractor
import ru.zveron.lots_feed.feed.domain.UpdateFeedInteractor
import ru.zveron.lots_feed.mappings.toSortType
import ru.zveron.lots_feed.mappings.toUiLot
import ru.zveron.lots_feed.mappings.toUiSortType

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
    private val queryInteractor: QueryInteractor,
) : ViewModel() {
    private var loadLotsJob: Job? = null

    private val _feedUiState = MutableStateFlow<LotsFeedUiState>(LotsFeedUiState.Loading)
    val feedUiState = _feedUiState.asStateFlow()

    val currentSortType = selectedSortTypeInteractor.selectedSortType
        .map { it.toUiSortType() }
        .stateIn(viewModelScope, SharingStarted.Lazily, SortType.DATE)

    val queryState = mutableStateOf("")

    private val _clearFocusFlow = MutableSharedFlow<Unit>(replay = 1 , extraBufferCapacity = 1)
    val clearFocusFlow = _clearFocusFlow.asSharedFlow()

    init {
        updateFeedInteractor.update()

        updateFeedInteractor.updateFlow
            .onEach { loadLots() }
            .launchIn(viewModelScope)

        queryInteractor.updateQueryFlow
            .onEach { (value, _) -> queryState.value = value }
            .distinctUntilChanged { old, new ->
                // UPDATE FEED IF INPUTS ARE DIFFERENT NOT BLANK OR IF NEW IS FORCED BY NOT USER
                new.second && (old.first == new.first || old.first.isBlank() && new.first.isBlank())
            }
            .debounce(100)
            .onEach { (_, byUser) ->
                if (byUser) {
                    updateFeedInteractor.update()
                } else {
                    _clearFocusFlow.tryEmit(Unit)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadLots() {
        loadLotsJob?.cancel()

        loadLotsJob = viewModelScope.launch {
            try {
                _feedUiState.value = LotsFeedUiState.Loading
                val lotsResponse = loadFeedInteractor.loadLots(queryState.value)
                val newLots = lotsResponse.map { it.toUiLot() }

                _feedUiState.value = LotsFeedUiState.Success(newLots, isRefreshing = false)
            } catch (e: Exception) {
                Log.e("Lots", "Error loading main page lots", e)
            }
        }
    }

    fun refreshLots() {
        loadLotsJob?.cancel()

        loadLotsJob = viewModelScope.launch {
            try {
                _feedUiState.update {
                    when (it) {
                        is LotsFeedUiState.Success -> it.copy(isRefreshing = true)
                        LotsFeedUiState.Loading -> it
                    }
                }
                val lotsResponse = loadFeedInteractor.loadLots(queryState.value)
                val newLots = lotsResponse.map { it.toUiLot() }

                _feedUiState.value = LotsFeedUiState.Success(newLots, isRefreshing = false)
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

    fun lotClicked(id: Long) {
        lotsFeedNavigator.goToLot(id)
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

    fun setQuery(value: String) {
        queryInteractor.updateQuery(value)
    }
}