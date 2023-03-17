package ru.zveron.lots_feed.filters_screen.domain.parameters

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.lot_forms.FiltersSelectedLotFormRepository
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersRepository
import ru.zveron.lots_feed.parameters.data.ParametersRepository

private const val DEFAULT_LOT_FORM_ID = 1

internal class FiltersUpdateParametersInteractor(
    private val parametersRepository: ParametersRepository,
    private val filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
    private val filtersSelectedLotFormRepository: FiltersSelectedLotFormRepository,
    private val filtersSelectedParametersRepository: FiltersSelectedParametersRepository,
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

    suspend fun loadParameters() {
        val currentCategorySelection = filtersSelectedCategoryRepository.currentCategorySelection.value
        val curretnLotFormSelection = filtersSelectedLotFormRepository.currentLotForm.value

        if (currentCategorySelection.innerCategory == null) {
            return
        }

        val categoryId = currentCategorySelection.innerCategory.id
        val lotFormId = curretnLotFormSelection?.id ?: DEFAULT_LOT_FORM_ID

        val parameters = parametersRepository.loadParameters(categoryId, lotFormId)

        filtersSelectedParametersRepository.updateParameters(parameters)
    }
}