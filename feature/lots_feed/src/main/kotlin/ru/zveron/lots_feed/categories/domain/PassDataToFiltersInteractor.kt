package ru.zveron.lots_feed.categories.domain

import ru.zveron.lots_feed.categories.data.SelectedCategoriesRepository
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryHolder
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormHolder
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersHolder

internal class PassDataToFiltersInteractor(
    private val selectedCategoriesRepository: SelectedCategoriesRepository,
    // TODO: add holders for parameters and lot forms

    private val filtersSelectedCategoryHolder: FiltersSelectedCategoryHolder,
    private val filtersSelectedLotFormHolder: FiltersSelectedLotFormHolder,
    private val filtersSelectedParametersHolder: FiltersSelectedParametersHolder,
) {
    fun passDataToFilters() {
        val currentCategorySelection = selectedCategoriesRepository.currentCategorySelection.value
        filtersSelectedCategoryHolder.selectRootCategory(currentCategorySelection.rootCategory?.id)
        filtersSelectedCategoryHolder.selectCategory(currentCategorySelection.innerCategory?.id)

        // TODO: pass actual parameter here
        filtersSelectedLotFormHolder.resetLotForm()
        filtersSelectedParametersHolder.updateParameters(emptyList())
    }
}