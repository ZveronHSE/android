package ru.zveron.choose_item

import kotlinx.coroutines.flow.StateFlow
import ru.zveron.design.resources.ZveronText

interface ChooseItemItemProvider {
    val uiState: StateFlow<ChooseItemUiState>

    fun itemPicked(id: Int)
}

sealed class ChooseItemUiState {
    object Loading: ChooseItemUiState()

    data class Success(val items: List<ChooseItem>): ChooseItemUiState()
}

data class ChooseItem(val id: Int, val title: ZveronText)