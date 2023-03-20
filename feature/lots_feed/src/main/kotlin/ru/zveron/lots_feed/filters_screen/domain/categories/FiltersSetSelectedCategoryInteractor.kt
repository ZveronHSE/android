package ru.zveron.lots_feed.filters_screen.domain.categories

import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersRepository
import ru.zveron.lots_feed.filters_screen.domain.lot_forms.FiltersUpdateLotFormsInteractor
import ru.zveron.lots_feed.filters_screen.domain.parameters.FiltersUpdateParametersInteractor

internal class FiltersSetSelectedCategoryInteractor(
    private val filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
    private val filtersSelectedLotFormRepository: FiltersSelectedLotFormRepository,
    private val filtersSelectedParametersRepository: FiltersSelectedParametersRepository,

    private val filtersUpdateCategoriesInteractor: FiltersUpdateCategoriesInteractor,
    private val filtersUpdateLotFormsInteractor: FiltersUpdateLotFormsInteractor,
    private val filtersUpdateParametersInteractor: FiltersUpdateParametersInteractor,
) {
    fun setRootCategory(id: Int) {
        filtersSelectedCategoryRepository.selectRootCategory(id)
        filtersSelectedLotFormRepository.resetLotForm()
        filtersSelectedParametersRepository.resetParamters()

        filtersUpdateCategoriesInteractor.updateChildCategories()
        filtersUpdateLotFormsInteractor.update()
    }

    fun setInnerCategory(id: Int) {
        filtersSelectedCategoryRepository.selectCategory(id)
        filtersSelectedLotFormRepository.resetLotForm()
        filtersSelectedParametersRepository.resetParamters()

        filtersUpdateParametersInteractor.update()
    }
}