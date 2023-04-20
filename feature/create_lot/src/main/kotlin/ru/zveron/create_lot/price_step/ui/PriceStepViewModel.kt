package ru.zveron.create_lot.price_step.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.zveron.create_lot.data.LotCreateInfoRepository
import ru.zveron.create_lot.price_step.PriceStepNavigator

internal class PriceStepViewModel(
    private val navigator: PriceStepNavigator,
    private val lotCreateInfoRepository: LotCreateInfoRepository,
): ViewModel() {

    val priceState = mutableStateOf<Int?>(null)

    val negotiatedState = mutableStateOf<Boolean>(false)

    val canContinueState = combine(
        snapshotFlow { priceState.value },
        snapshotFlow { negotiatedState.value },
    ) { price, isNegotiated ->
        isNegotiated || (price != null && price > 0)
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun setPrice(price: Int?) {
        priceState.value = price
    }

    fun toggleNegotiated() {
        negotiatedState.value = !negotiatedState.value
    }

    fun continueClicked() {
        val price = priceState.value
        val isNegotiated = negotiatedState.value

        require(isNegotiated || (price != null && price > 0)) {
            IllegalStateException("Price should be inputted or negotiated must be checked")
        }

        val actualPrice = if (isNegotiated) 0 else price!!

        lotCreateInfoRepository.setPrice(actualPrice)

        navigator.completePriceStep()
    }
}