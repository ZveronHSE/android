package ru.zveron.user_profile.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.wrappers.ListWrapper
import ru.zveron.user_profile.R

@Stable
sealed class UserProfileUiState {
    object Error: UserProfileUiState()

    object Loading: UserProfileUiState()

    @Stable
    data class Success(
        val photo: ZveronImage,
        val displayedName: ZveronText,
        val address: String,
        val rating: Double,
        val reviewAmount: Int,
        val currentLots: ListWrapper<LotUiState>,
        val currentTab: UserProfileTab,
    ): UserProfileUiState()
}

@Stable
data class LotUiState(
    val id: Long,
    val title: String,
    val price: String,
    val date: String,
    val image: ZveronImage,
    val isLiked: MutableState<Boolean>,
)

enum class UserProfileTab(@StringRes val resInt: Int) {
    ACTIVE(R.string.active_tab),
    ENDED(R.string.ended_tab),
}
