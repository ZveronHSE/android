package ru.zveron.lots_feed.filters_screen.ui.lot_form

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.R
import ru.zveron.lots_feed.choose_item.ChooseItemHolder
import ru.zveron.lots_feed.filters_screen.FiltersNavigator
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormRepository
import ru.zveron.lots_feed.filters_screen.domain.lot_forms.FiltersUpdateLotFormsInteractor
import ru.zveron.lots_feed.filters_screen.domain.lot_forms.LotFormItemProvider

internal class LotFormViewModel(
    filtersSelectedLotFormRepository: FiltersSelectedLotFormRepository,
    filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
    private val chooseItemHolder: ChooseItemHolder,
    private val lotFormItemProvider: LotFormItemProvider,
    private val filtersNavigator: FiltersNavigator,
    private val filtersUpdateLotFormsInteractor: FiltersUpdateLotFormsInteractor,
): ViewModel() {

    init {
        filtersUpdateLotFormsInteractor.updateFlow
            .onEach { loadLotForms() }
            .launchIn(viewModelScope)
    }

    val uiState = combine(
        filtersSelectedCategoryRepository.currentCategorySelection,
        filtersSelectedLotFormRepository.currentLotForm
    ) { categorySelection, lotForm ->
        if (categorySelection.rootCategory == null) {
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

    fun lotFormRowClicked() {
        val title = getLotFormTitle() ?: return
        chooseItemHolder.setCurrentItemItemProvider(lotFormItemProvider)
        filtersNavigator.chooseItem(title)
    }

    private fun getLotFormTitle(): ZveronText? {
        val state = uiState.value as? LotFormUiState.Show ?: return null
        return state.selectedLotFormTitle
    }

    private fun loadLotForms() {
        viewModelScope.launch {
            try {
                filtersUpdateLotFormsInteractor.loadChildrenLotForms()
            } catch (e: Exception) {
                Log.e("Lot forms", "Error loading lot forms", e)
            }
        }
    }
}