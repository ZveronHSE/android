package ru.zveron.favorites.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.zveron.favorites.ui.state.FavoritesCategoriesUiState

class FavoritesViewModel(

): ViewModel() {
    private val _categoriesUiState = MutableStateFlow<FavoritesCategoriesUiState>(
        FavoritesCategoriesUiState.Loading
    )

    fun onLotLikeClick(id: Int) {

    }
}