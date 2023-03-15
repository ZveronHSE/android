package ru.zveron.lots_feed.filters_screen.ui.lot_form

import androidx.compose.runtime.Immutable
import ru.zveron.design.resources.ZveronText

@Immutable
sealed class LotFormUiState {
    @Immutable
    object Hidden: LotFormUiState()

    @Immutable
    data class Show(val selectedLotFormTitle: ZveronText): LotFormUiState()
}