package ru.zveron.favorites.ui.state

import androidx.compose.runtime.MutableState
import ru.zveron.design.resources.ZveronImage

sealed class FavoritesContentState

sealed class FavoritesLotsUiState: FavoritesContentState() {
    object Loading: FavoritesLotsUiState()

    data class Success(val lots: List<LotUiState>, val isRefreshing: Boolean = false): FavoritesLotsUiState()

    object Error: FavoritesLotsUiState()
}

data class LotUiState(
    val id: Long,
    val title: String,
    val price: String,
    val date: String,
    val image: ZveronImage,
    val isLiked: MutableState<Boolean>,
    val isActive: Boolean,
)