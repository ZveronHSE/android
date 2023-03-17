package ru.zveron.lots_feed.filters_screen.domain.lot_forms

import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersRepository
import ru.zveron.lots_feed.filters_screen.domain.parameters.FiltersUpdateParametersInteractor

internal class FiltersSetSelectedLotFormInteractor(
    private val filtersSelectedLotFormRepository: FiltersSelectedLotFormRepository,
    private val filtersSelectedParametersRepository: FiltersSelectedParametersRepository,
    private val filtersUpdateParametersInteractor: FiltersUpdateParametersInteractor,
) {
    fun selectLotForm(id: Int) {
        filtersSelectedLotFormRepository.selectLotForm(id)
        filtersSelectedParametersRepository.resetParamters()
        filtersUpdateParametersInteractor.update()
    }
}