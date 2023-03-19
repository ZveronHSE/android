package ru.zveron.lots_feed.categories.domain

import ru.zveron.lots_feed.categories.data.SelectedCategoriesRepository
import ru.zveron.lots_feed.feed.data.lot_forms.SelectedLotFormRepository
import ru.zveron.lots_feed.feed.data.parameters.SelectedParametersRepository
import ru.zveron.lots_feed.feed.data.sort_type.SelectedSortTypeRepository
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersParametersLoadingRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersRepository
import ru.zveron.lots_feed.filters_screen.data.sort_types.FiltersSelectedSortTypeRepository
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

    private val filtersParametersLoadingRepository: FiltersParametersLoadingRepository,
) {
    fun passDataToFilters() {
        val currentCategorySelection = selectedCategoriesRepository.currentCategorySelection.value

        filtersSelectedCategoryRepository.selectRootCategory(currentCategorySelection.rootCategory?.id)
        filtersSelectedCategoryRepository.selectCategory(currentCategorySelection.innerCategory?.id)
        filtersSelectedSortTypeRepository.selectSortType(selectedSortTypeRepository.currentSortType.value)

        val currentLotForm = selectedLotFormRepository.currentLotForm.value
        if (currentLotForm == null) {
            filtersSelectedLotFormRepository.resetLotForm()
        } else {
            filtersSelectedLotFormRepository.selectLotForm(currentLotForm.id)
        }

        val currentFilters = selectedParametersRepository.parametersState.value
        filtersSelectedParametersRepository.updateParameters(currentFilters.map { it.parameter })
        currentFilters.forEach {
            it.value?.let { value ->
                filtersSelectedParametersRepository.setParameterValue(it.parameter.id, value)
            }
        }

        filtersUpdateLotFormsInteractor.update()
        if (currentCategorySelection.innerCategory != null && currentFilters.isEmpty()) {
            filtersUpdateParametersInteractor.update()
        } else if (currentFilters.isNotEmpty()) {
            filtersParametersLoadingRepository.updateIsLoading(false)
        }
    }
}