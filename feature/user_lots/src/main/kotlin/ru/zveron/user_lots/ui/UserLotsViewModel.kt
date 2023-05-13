package ru.zveron.user_lots.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class UserLotsViewModel(
    private val tabNavigator: TabNavigator,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserLotsUiState(currentTab = UserLotTab.ACTIVE))
    val uiState = _uiState.asStateFlow()

    fun onTabClick(userLotTab: UserLotTab) {
        if (userLotTab == _uiState.value.currentTab) {
            return
        }

        _uiState.update { it.copy(currentTab = userLotTab) }
        when (userLotTab) {
            UserLotTab.ACTIVE -> tabNavigator.openActiveTab()
            UserLotTab.ARCHIVE -> tabNavigator.openArchiveTab()
        }
    }
}
