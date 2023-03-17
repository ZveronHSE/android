package ru.zveron.lots_feed.filters_screen.domain.lot_forms

import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormRepository

class FiltersSetSelectedLotFormInteractor(
    private val filtersSelectedLotFormRepository: FiltersSelectedLotFormRepository,
) {
    fun selectLotForm(id: Int) {
        filtersSelectedLotFormRepository.selectLotForm(id)
        // TODO: reset and load parameters
    }
}