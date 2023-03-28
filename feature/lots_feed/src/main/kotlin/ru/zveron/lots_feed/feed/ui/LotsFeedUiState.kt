package ru.zveron.lots_feed.feed.ui

import androidx.compose.runtime.MutableState
import ru.zveron.design.resources.ZveronImage

sealed class LotsFeedUiState {
    object Loading: LotsFeedUiState()

    data class Success(val lots: List<LotUiState>): LotsFeedUiState()
}


data class LotUiState(
    val id: Long,
    val title: String,
    val price: String,
    val date: String,
    val image: ZveronImage,
    val isLiked: MutableState<Boolean>,
)