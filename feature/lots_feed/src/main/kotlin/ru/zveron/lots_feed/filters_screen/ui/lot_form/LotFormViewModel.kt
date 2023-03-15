package ru.zveron.lots_feed.filters_screen.ui.lot_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.R
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryHolder
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormHolder

class LotFormViewModel(
    filtersSelectedLotFormHolder: FiltersSelectedLotFormHolder,
    filtersSelectedCategoryHolder: FiltersSelectedCategoryHolder,
): ViewModel() {
    val uiState = combine(
        filtersSelectedCategoryHolder.currentCategorySelection,
        filtersSelectedLotFormHolder.currentLotForm
    ) { categorySelection, lotForm ->
        if (categorySelection.innerCategory == null) {
            LotFormUiState.Hidden
        } else {
            val text = if (lotForm == null) {
                ZveronText.RawResource(R.string.lot_form_title)
            } else {
                ZveronText.RawString(lotForm.title)
            }
            LotFormUiState.Show(text)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, LotFormUiState.Hidden)
}