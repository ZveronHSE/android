package ru.zveron.favorites.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.categories.data.CategoryRepository
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.R as DesignR
import ru.zveron.favorites.domain.LoadCategoryFavoriteLotsInteractor
import ru.zveron.favorites.domain.RemoveLotFromFavoritesInteractor
import ru.zveron.favorites.ui.state.CategoryUiState
import ru.zveron.favorites.ui.state.FavoritesCategoriesUiState
import ru.zveron.favorites.ui.state.FavoritesLotsUiState
import ru.zveron.favorites.ui.state.LotUiState

internal class FavoritesViewModel(
    private val loadCategoryFavoriteLotsInteractor: LoadCategoryFavoriteLotsInteractor,
    private val removeLotFromFavoritesInteractor: RemoveLotFromFavoritesInteractor,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    private val _categoriesUiState =
        MutableStateFlow<FavoritesCategoriesUiState>(FavoritesCategoriesUiState.Loading)
    val categoriesUiState = _categoriesUiState.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    private var _contentStates: MutableStateFlow<Map<Int, FavoritesLotsUiState>> = MutableStateFlow(
        emptyMap()
    )

    val contentUiState = combine(
        _selectedCategoryId.filterNotNull(),
        _contentStates
    ) { selectedCategoryId, contentStates ->
        contentStates[selectedCategoryId]
    }.filterNotNull().stateIn(viewModelScope, SharingStarted.Lazily, FavoritesLotsUiState.Loading)

    init {
        loadRootCategories()
    }

    private fun loadRootCategories() {
        viewModelScope.launch {
            _categoriesUiState.update { FavoritesCategoriesUiState.Loading }
            try {
                val rootCategories = categoryRepository.loadRootCategories()
                val uiCategories = rootCategories.map {
                    CategoryUiState(
                        id = it.id,
                        title = it.name,
                    )
                }
                val activeCategoryId = rootCategories[0].id

                _categoriesUiState.update {
                    FavoritesCategoriesUiState.Success(
                        uiCategories,
                        activeCategoryId
                    )
                }

                _selectedCategoryId.update { activeCategoryId }
                loadFavoriteLotsForCategory(activeCategoryId)
            } catch (e: Exception) {
                Log.e("Favorites", "Error loading root categories", e)
            }
        }
    }

    private fun loadFavoriteLotsForCategory(categoryId: Int) {
        viewModelScope.launch {
            updateUiStateForCategory(categoryId, FavoritesLotsUiState.Loading)
            try {
                val lots = loadCategoryFavoriteLotsInteractor.loadFavoriteLots(categoryId)
                val uiLots = lots.map {
                    LotUiState(
                        id = it.id,
                        title = it.title,
                        price = it.price,
                        date = it.publicationDate,
                        image = ZveronImage.ResourceImage(DesignR.drawable.cool_dog),
                        isLiked = mutableStateOf(it.isFavorite),
                    )
                }
                val successState = FavoritesLotsUiState.Success(uiLots)
                updateUiStateForCategory(categoryId, successState)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("Favorites", "Error loading favorite lots", e)
                updateUiStateForCategory(categoryId, FavoritesLotsUiState.Error)
            }
        }
    }

    private fun updateUiStateForCategory(categoryId: Int, state: FavoritesLotsUiState) {
        _contentStates.update {
            buildMap {
                putAll(it)
                put(categoryId, state)
            }
        }
    }

    fun categorySelected(categoryId: Int) {
        _selectedCategoryId.update { categoryId }
        if (_contentStates.value[categoryId] == null) {
            loadFavoriteLotsForCategory(categoryId)
        }
    }

    fun onLotLikeClick(lotId: Long) {
        _contentStates.update { states ->
            val currentCategoryId = _selectedCategoryId.value
            val newCategoryState = states[currentCategoryId]?.let { lotsState ->
                when (lotsState) {
                    FavoritesLotsUiState.Error, FavoritesLotsUiState.Loading -> lotsState
                    is FavoritesLotsUiState.Success -> {
                        val elementToRemove = lotsState.lots.findLast { it.id == lotId }
                        val lots = buildList {
                            addAll(lotsState.lots)
                            elementToRemove?.let { remove(elementToRemove) }
                        }
                        FavoritesLotsUiState.Success(lots)
                    }
                }
            }
            buildMap {
                putAll(states)
                if (currentCategoryId != null && newCategoryState != null) {
                    put(currentCategoryId, newCategoryState)
                }
            }
        }
        viewModelScope.launch {
            removeLotFromFavoritesInteractor.removeLotFromFavorites(lotId)
        }
    }

    fun retryClicked() {
        val categoryId = _selectedCategoryId.value ?: return
        loadFavoriteLotsForCategory(categoryId)
    }
}