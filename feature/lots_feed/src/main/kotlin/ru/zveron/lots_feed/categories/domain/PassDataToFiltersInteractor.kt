package ru.zveron.lots_feed.categories.domain

import ru.zveron.categories.data.CategorySelection
import ru.zveron.categories.data.SelectedCategoriesRepository
import ru.zveron.lots_feed.feed.data.lot_forms.SelectedLotFormRepository
import ru.zveron.lots_feed.feed.data.parameters.SelectedParametersRepository
import ru.zveron.lots_feed.feed.data.sort_type.SelectedSortTypeRepository
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersParametersLoadingRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersRepository
import ru.zveron.lots_feed.filters_screen.data.sort_types.FiltersSelectedSortTypeRepository
import ru.zveron.lots_feed.filters_screen.domain.categories.FiltersUpdateCategoriesInteractor
import ru.zveron.lots_feed.filters_screen.domain.lot_forms.FiltersUpdateLotFormsInteractor
import ru.zveron.lots_feed.filters_screen.domain.parameters.FiltersUpdateParametersInteractor

internal class PassDataToFiltersInteractor(
    private val selectedCategoriesRepository: SelectedCategoriesRepository,
    private val selectedSortTypeRepository: SelectedSortTypeRepository,
    private val selectedParametersRepository: SelectedParametersRepository,
    private val selectedLotFormRepository: SelectedLotFormRepository,

    private val filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
    private val filtersSelectedLotFormRepository: FiltersSelectedLotFormRepository,
    private val filtersSelectedParametersRepository: FiltersSelectedParametersRepository,
    private val filtersSelectedSortTypeRepository: FiltersSelectedSortTypeRepository,

    private val filtersUpdateLotFormsInteractor: FiltersUpdateLotFormsInteractor,
    private val filtersUpdateParametersInteractor: FiltersUpdateParametersInteractor,
    private val filtersUpdateCategoriesInteractor: FiltersUpdateCategoriesInteractor,

    private val filtersParametersLoadingRepository: FiltersParametersLoadingRepository,
) {
    fun passDataToFilters() {
        val currentCategorySelection = selectedCategoriesRepository.currentCategorySelection.value

        passCategory(currentCategorySelection)
        filtersSelectedSortTypeRepository.selectSortType(selectedSortTypeRepository.currentSortType.value)
        passLotForm()
        passFilters(currentCategorySelection)
    }

    private fun passCategory(currentCategorySelection: CategorySelection) {
        filtersSelectedCategoryRepository.selectRootCategory(currentCategorySelection.rootCategory?.id)
        filtersSelectedCategoryRepository.selectCategory(currentCategorySelection.innerCategory?.id)

        if (currentCategorySelection.rootCategory != null) {
            filtersUpdateCategoriesInteractor.updateChildCategories()
        } else {
            filtersUpdateCategoriesInteractor.updateRootCategories()
        }
    }

    private fun passLotForm() {
        val currentLotForm = selectedLotFormRepository.currentLotForm.value
        if (currentLotForm == null) {
            filtersSelectedLotFormRepository.resetLotForm()
        } else {
            filtersSelectedLotFormRepository.selectLotForm(currentLotForm.id)
        }
        filtersUpdateLotFormsInteractor.update()
    }

    private fun passFilters(currentCategorySelection: CategorySelection) {
        val currentParameters = selectedParametersRepository.parametersState.value
        filtersSelectedParametersRepository.updateParameters(currentParameters.map { it.parameter })
        currentParameters.forEach {
            it.value?.let { value ->
                filtersSelectedParametersRepository.setParameterValue(it.parameter.id, value)
            }
        }

        if (currentCategorySelection.innerCategory != null && currentParameters.isEmpty()) {
            filtersUpdateParametersInteractor.update()
        } else if (currentParameters.isNotEmpty()) {
            filtersParametersLoadingRepository.updateIsLoading(false)
        }
    }
}