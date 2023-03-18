package ru.zveron.lots_feed.categories.domain

import ru.zveron.lots_feed.categories.data.SelectedCategoriesRepository
import ru.zveron.lots_feed.feed.data.sort_type.SelectedSortTypeRepository
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersRepository
import ru.zveron.lots_feed.filters_screen.data.sort_types.FiltersSelectedSortTypeRepository

internal class PassDataToFiltersInteractor(
    private val selectedCategoriesRepository: SelectedCategoriesRepository,
    private val selectedSortTypeRepository: SelectedSortTypeRepository,
    // TODO: add holders for parameters and lot forms

    private val filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
    private val filtersSelectedLotFormRepository: FiltersSelectedLotFormRepository,
    private val filtersSelectedParametersRepository: FiltersSelectedParametersRepository,
    private val filtersSelectedSortTypeRepository: FiltersSelectedSortTypeRepository,
) {
    fun passDataToFilters() {
        val currentCategorySelection = selectedCategoriesRepository.currentCategorySelection.value
        filtersSelectedCategoryRepository.selectRootCategory(currentCategorySelection.rootCategory?.id)
        filtersSelectedCategoryRepository.selectCategory(currentCategorySelection.innerCategory?.id)
        filtersSelectedSortTypeRepository.selectSortType(selectedSortTypeRepository.currentSortType.value)

        // TODO: pass actual parameter here
        filtersSelectedLotFormRepository.resetLotForm()
        filtersSelectedParametersRepository.updateParameters(emptyList())
        filtersSelectedParametersRepository.resetParamters()
    }
}