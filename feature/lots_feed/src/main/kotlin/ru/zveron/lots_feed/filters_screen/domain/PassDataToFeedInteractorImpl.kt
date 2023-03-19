package ru.zveron.lots_feed.filters_screen.domain

import ru.zveron.lots_feed.categories.data.SelectedCategoriesRepository
import ru.zveron.lots_feed.feed.data.lot_forms.SelectedLotFormRepository
import ru.zveron.lots_feed.feed.data.parameters.ParametersLoadingRepository
import ru.zveron.lots_feed.feed.data.parameters.SelectedParametersRepository
import ru.zveron.lots_feed.feed.data.sort_type.SelectedSortTypeRepository
import ru.zveron.lots_feed.feed.domain.UpdateFeedInteractor
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersRepository
import ru.zveron.lots_feed.filters_screen.data.sort_types.FiltersSelectedSortTypeRepository

internal class PassDataToFeedInteractorImpl(
    private val filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
    private val filtersSelectedLotFormRepository: FiltersSelectedLotFormRepository,
    private val filtersSelectedParametersRepository: FiltersSelectedParametersRepository,
    private val filtersSelectedSortTypeRepository: FiltersSelectedSortTypeRepository,

    private val selectedCategoriesRepository: SelectedCategoriesRepository,
    private val selectedSortTypeRepository: SelectedSortTypeRepository,
    private val selectedParametersRepository: SelectedParametersRepository,
    private val selectedLotFormRepository: SelectedLotFormRepository,

    private val parametersLoadingRepository: ParametersLoadingRepository,

    private val updateFeedInteractor: UpdateFeedInteractor,
): PassDataToFeedInteractor {
    override fun passDataToLotsFeed() {
        selectedCategoriesRepository.setCategorySelection(filtersSelectedCategoryRepository.currentCategorySelection.value)
        selectedSortTypeRepository.selectSortType(filtersSelectedSortTypeRepository.selectedSortType.value)
        passParameters()
        passLotForm()

        updateFeedInteractor.update()
    }

    private fun passParameters() {
        val parameterState = filtersSelectedParametersRepository.parametersState.value
        selectedParametersRepository.updateParameters(parameterState.map { it.parameter })

        filtersSelectedParametersRepository.parametersState.value.forEach { state ->
            state.value?.let {
                selectedParametersRepository.setParameterValue(state.parameter.id, it)
            }
        }

        if (parameterState.isNotEmpty()) {
            parametersLoadingRepository.updateIsLoading(false)
        }
    }

    private fun passLotForm() {
        val currentLotForm = filtersSelectedLotFormRepository.currentLotForm.value
        if (currentLotForm == null) {
            selectedLotFormRepository.resetLotForm()
        } else {
            selectedLotFormRepository.selectLotForm(currentLotForm.id)
        }
    }
}