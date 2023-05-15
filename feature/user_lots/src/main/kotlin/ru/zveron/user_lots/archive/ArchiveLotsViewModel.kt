package ru.zveron.user_lots.archive

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.user_lots.UserLotsNavigator
import ru.zveron.user_lots.domain.GetUserLotsInteractor
import ru.zveron.user_lots.ui.BaseLotsViewModel
import ru.zveron.user_lots.ui.LotsUiState

internal class ArchiveLotsViewModel (
    private val userLotsNavigator: UserLotsNavigator,
    private val getUserLotsInteractor: GetUserLotsInteractor,
): BaseLotsViewModel() {
    init {
        loadLots()
    }

    private fun loadLots() {
        viewModelScope.launch {
            _uiState.update { LotsUiState.Loading }
            try {
                val userLots = getUserLotsInteractor.getArchiveLots()
                val uiUserLots = mapToUiUserLots(userLots)
                _uiState.update { uiUserLots }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("User lots", "Error loading arctive lots", e)
                _uiState.update { LotsUiState.Error }
            }
        }
    }

    fun refreshLots() {
        viewModelScope.launch {
            _uiState.update {
                when (it) {
                    is LotsUiState.Success -> it.copy(isRefreshing = true)
                    else -> it
                }
            }

            try {
                val userLots = getUserLotsInteractor.getActiveLots()
                val uiUserLots = mapToUiUserLots(userLots)
                _uiState.update { uiUserLots }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("User lots", "Error loading arctive lots", e)
                _uiState.update { LotsUiState.Error }
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
}