package ru.zveron.lots_feed.filters_screen.domain.lot_forms

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.zveron.choose_item.ChooseItem
import ru.zveron.choose_item.ChooseItemItemProvider
import ru.zveron.choose_item.ChooseItemUiState
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.filters_screen.data.lot_forms.ChildLotFormState
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersChildrenLotFormRepository

internal class LotFormItemProvider(
    filtersChildrenLotFormRepository: FiltersChildrenLotFormRepository,
    private val filtersSetSelectedLotFormInteractor: FiltersSetSelectedLotFormInteractor,
): ChooseItemItemProvider {
    private val scope = CoroutineScope(Dispatchers.IO)

    override val uiState: StateFlow<ChooseItemUiState> = filtersChildrenLotFormRepository.childrenLotFormState.map {
        when (it) {
            ChildLotFormState.Loading -> ChooseItemUiState.Loading
            is ChildLotFormState.Success -> {
                val items = it.lotForms.map { lotForm -> ChooseItem(lotForm.id, ZveronText.RawString(lotForm.title)) }
                ChooseItemUiState.Success(items)
            }
        }
    }.stateIn(scope, SharingStarted.Lazily, ChooseItemUiState.Loading)

    override fun itemPicked(id: Int) {
        filtersSetSelectedLotFormInteractor.selectLotForm(id)
    }
}