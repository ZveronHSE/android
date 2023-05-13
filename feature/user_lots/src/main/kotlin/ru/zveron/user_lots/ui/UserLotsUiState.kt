package ru.zveron.user_lots.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.wrappers.ListWrapper
import ru.zveron.user_lots.R

@Immutable
data class UserLotsUiState(
    val currentTab: UserLotTab,
)

@Immutable
internal sealed class LotsUiState {
    data class Success(
        val currentLots: ListWrapper<LotUiState>,
        val isRefreshing: Boolean = false,
    ): LotsUiState()

    object Loading: LotsUiState()

    object Error: LotsUiState()
}

data class LotUiState(
    val id: Long,
    val title: String,
    val price: String,
    val image: ZveronImage,
    val isActive: Boolean,
//    val views: Int,
//    val likes: Int,
)

enum class UserLotTab(@StringRes val title: Int) {
    ACTIVE(R.string.active_lots_title),
    ARCHIVE(R.string.archive_lots_title),
}