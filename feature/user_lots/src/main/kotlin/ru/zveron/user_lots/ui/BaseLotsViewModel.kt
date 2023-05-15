package ru.zveron.user_lots.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.wrappers.ListWrapper
import ru.zveron.user_lots.domain.Lot

internal open class BaseLotsViewModel: ViewModel() {
    protected val _uiState = MutableStateFlow<LotsUiState>(LotsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    protected fun mapToUiUserLots(lots: List<Lot>): LotsUiState.Success {
        return LotsUiState.Success(
            currentLots = ListWrapper(lots.map { mapToLotUiState(it) }),
        )
    }

    private fun mapToLotUiState(lot: Lot): LotUiState {
        return LotUiState(
            id = lot.id,
            title = lot.title,
            price = lot.price,
            image = ZveronImage.RemoteImage(lot.imageUrl),
            isActive = lot.isActive,
//            views = lot.views,
//            likes = lot.likes,
        )
    }
}