package ru.zveron.user_lots.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.wrappers.ListWrapper
import ru.zveron.user_lots.R

@Immutable
sealed class UserLotsUiState {
    data class Success(
        val currentLots: ListWrapper<LotUiState>,
        val currentTab: UserLotTab,
        val isRefreshing: Boolean = false,
    ): UserLotsUiState()

    object Loading: UserLotsUiState()

    object Error: UserLotsUiState()
}

data class LotUiState(
    val id: Long,
    val title: String,
    val price: String,
    val image: ZveronImage,
    val isActive: Boolean,
    val views: Int,
    val likes: Int,
)

enum class UserLotTab(@StringRes val title: Int) {
    ACTIVE(R.string.active_lots_title),
    ARCHIVE(R.string.archive_lots_title),
}