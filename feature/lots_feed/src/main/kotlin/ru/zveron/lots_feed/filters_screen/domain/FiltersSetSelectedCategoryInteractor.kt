package ru.zveron.lots_feed.filters_screen.domain

import ru.zveron.lots_feed.filters_screen.data.categories.FiltersChildrenCategoryHolder
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryHolder
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersChildrenLotFormHolder
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormHolder

class FiltersSetSelectedCategoryInteractor(
    private val filtersSelectedCategoryHolder: FiltersSelectedCategoryHolder,
    private val filtersSelectedLotFormHolder: FiltersSelectedLotFormHolder,
    private val filtersChildrenLotFormHolder: FiltersChildrenLotFormHolder,
    private val filtersChildrenCategoryHolder: FiltersChildrenCategoryHolder,
) {
    suspend fun setRootCategory(id: Int) {
        filtersSelectedCategoryHolder.selectRootCategory(id)
        filtersSelectedLotFormHolder.resetLotForm()
        filtersChildrenCategoryHolder.updateChildrenCategory(id)
    }

    suspend fun setInnerCategory(id: Int) {
        filtersSelectedCategoryHolder.selectCategory(id)
        filtersSelectedLotFormHolder.resetLotForm()
        filtersChildrenLotFormHolder.updateChildrenLotForms(id)
    }
}