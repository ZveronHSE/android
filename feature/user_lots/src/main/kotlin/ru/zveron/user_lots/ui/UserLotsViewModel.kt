package ru.zveron.user_lots.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.wrappers.ListWrapper
import ru.zveron.user_lots.UserLotsNavigator
import ru.zveron.user_lots.domain.GetUserLotsInteractor
import ru.zveron.user_lots.domain.Lot
import ru.zveron.user_lots.domain.UserLots

class UserLotsViewModel(
    private val getUserLotsInteractor: GetUserLotsInteractor,
    private val userLotsNavigator: UserLotsNavigator,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UserLotsUiState>(UserLotsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _userLots = MutableStateFlow<UiUserLots?>(null)
    private val _currentTab = MutableStateFlow(UserLotTab.ACTIVE)

    init {
        loadLots()
    }

    private fun loadLots() {
        viewModelScope.launch {
            _uiState.update { UserLotsUiState.Loading }
            try {
                val userLots = getUserLotsInteractor.getUserLots()
                val uiUserLots = mapToUiUserLots(userLots)
                _userLots.update { uiUserLots }
                _currentTab.update { UserLotTab.ACTIVE }

                _uiState.update {
                    UserLotsUiState.Success(
                        currentLots = uiUserLots.activeLots,
                        UserLotTab.ACTIVE,
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { UserLotsUiState.Error }
            }
        }
    }

    fun refreshLots() {
        viewModelScope.launch {
            _uiState.update {
                when (it) {
                    is UserLotsUiState.Success -> it.copy(isRefreshing = true)
                    else -> it
                }
            }
            try {
                val userLots = getUserLotsInteractor.getUserLots()
                val uiUserLots = mapToUiUserLots(userLots)
                _userLots.update { uiUserLots }

                _uiState.update {
                    UserLotsUiState.Success(
                        currentLots = uiUserLots.activeLots,
                        _currentTab.value,
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { UserLotsUiState.Error }
            }
        }
    }
    fun onTabClick(userLotTab: UserLotTab) {
        if (userLotTab == _currentTab.value) {
            return
        }

        _currentTab.update { userLotTab }
        val userLots = _userLots.value!!

        val lots = when (userLotTab) {
            UserLotTab.ACTIVE -> userLots.activeLots
            UserLotTab.ARCHIVE -> userLots.archiveLots
        }

        _uiState.update {
            when (it) {
                UserLotsUiState.Error, UserLotsUiState.Loading -> it
                is UserLotsUiState.Success -> UserLotsUiState.Success(
                    currentLots = lots,
                    currentTab = userLotTab,
                )
            }
        }
    }

    fun onLotClick(id: Long) {
        userLotsNavigator.openLot(id)
    }

    fun onRetryClick() {
        loadLots()
    }

    fun onAddLotClick() {
        userLotsNavigator.createLot()
    }

    private fun mapToUiUserLots(userLots: UserLots): UiUserLots {
        return UiUserLots(
            activeLots = ListWrapper(userLots.activeLots.map { mapToLotUiState(it) }),
            archiveLots = ListWrapper(userLots.archivedLots.map { mapToLotUiState(it) }),
        )
    }

    private fun mapToLotUiState(lot: Lot): LotUiState {
        return LotUiState(
            id = lot.id,
            title = lot.title,
            price = lot.price,
            image = ZveronImage.RemoteImage(lot.imageUrl),
            isActive = lot.isActive,
            views = lot.views,
            likes = lot.likes,
        )
    }
}

data class UiUserLots(
    val activeLots: ListWrapper<LotUiState>,
    val archiveLots: ListWrapper<LotUiState>,
)