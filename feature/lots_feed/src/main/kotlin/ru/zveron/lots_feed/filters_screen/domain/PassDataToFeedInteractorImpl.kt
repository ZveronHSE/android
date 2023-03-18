package ru.zveron.lots_feed.filters_screen.domain

import ru.zveron.lots_feed.categories.data.SelectedCategoriesRepository
import ru.zveron.lots_feed.feed.domain.UpdateFeedInteractor
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersRepository

internal class PassDataToFeedInteractorImpl(
    private val filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
    private val filtersSelectedLotFormRepository: FiltersSelectedLotFormRepository,
    private val filtersSelectedParametersRepository: FiltersSelectedParametersRepository,

    private val selectedCategoriesRepository: SelectedCategoriesRepository,

    private val updateFeedInteractor: UpdateFeedInteractor,
): PassDataToFeedInteractor {
    override fun passDataToLotsFeed() {
        selectedCategoriesRepository.setCategorySelection(filtersSelectedCategoryRepository.currentCategorySelection.value)
        // TODO: pass lot form and parameters

        updateFeedInteractor.update()
    }
}