package ru.zveron.lots_feed.filters_screen.domain.lot_forms

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersChildrenLotFormRepository

class FiltersUpdateLotFormsInteractor(
    private val filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
    private val filtersChildrenLotFormRepository: FiltersChildrenLotFormRepository,
) {
    /**
     * Explanations on replay and extrabuffercapacity parameters
     *
     * This flow will be emmited as a result of calling update method from lots feed screen,
     * so replay needs to be set to be emitted when filters screen is attached.
     * Extra buffer is needed so lots feed screen wont be buffer overflowed
     */
    private val _updateFlow = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1)
    val updateFlow = _updateFlow.asSharedFlow()

    fun update() {
        _updateFlow.tryEmit(Unit)
    }

    suspend fun loadChildrenLotForms() {
        val categorySelection = filtersSelectedCategoryRepository.currentCategorySelection.value
        val currentCategoryId = categorySelection.getCurrentCategory()?.id ?: return

        filtersChildrenLotFormRepository.updateChildrenLotForms(currentCategoryId)
    }
}