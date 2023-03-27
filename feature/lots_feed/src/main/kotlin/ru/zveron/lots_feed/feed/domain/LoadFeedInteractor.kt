package ru.zveron.lots_feed.feed.domain

import ru.zveron.categories.data.SelectedCategoriesRepository
import ru.zveron.lots_feed.feed.data.feed.LotsFeedRepository
import ru.zveron.lots_feed.feed.data.lot_forms.SelectedLotFormRepository
import ru.zveron.lots_feed.feed.data.parameters.SelectedParametersRepository
import ru.zveron.lots_feed.models.filters.Filter
import ru.zveron.lots_feed.models.filters.FilterField
import ru.zveron.lots_feed.models.filters.FilterOperation
import ru.zveron.models.lots.Lot

class LoadFeedInteractor(
    private val lotsFeedRepository: LotsFeedRepository,

    private val selectedSortTypeInteractor: SelectSortTypeInteractor,
    private val selectedCategoriesRepository: SelectedCategoriesRepository,
    private val selectedParametersRepository: SelectedParametersRepository,
    private val selectedLotFormRepository: SelectedLotFormRepository,
) {
    suspend fun loadLots(
        filters: List<Filter>,
    ): List<Lot> {
        val sortType = selectedSortTypeInteractor.selectedSortType.value

        val lotForm = selectedLotFormRepository.currentLotForm.value
        val actualFilters = if (lotForm != null) {
            filters + Filter(
                FilterField.LOT_FORM_ID,
                FilterOperation.EQUALITY,
                lotForm.id.toString(),
            )
        } else {
            filters
        }

        val category = selectedCategoriesRepository.currentCategorySelection.value.getCurrentCategory()
        val parameters = selectedParametersRepository.parametersState.value
            .filter { state ->
                state.value != null
            }
            .takeIf {
                it.isNotEmpty()
            }

        return lotsFeedRepository.loadLots(
            filters = actualFilters,
            sortType = sortType,
            parameters = parameters,
            category = category,
            // TODO: add query
            query = null,
        )
    }
}