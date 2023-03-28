package ru.zveron.lots_feed.feed.domain

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.zveron.categories.data.SelectedCategoriesRepository
import ru.zveron.lots_feed.feed.data.lot_forms.SelectedLotFormRepository
import ru.zveron.lots_feed.feed.data.parameters.SelectedParametersRepository
import ru.zveron.lots_feed.parameters.data.ParametersRepository

internal class UpdateParametersInteractorImpl(
    private val parametersRepository: ParametersRepository,
    private val selectedCategoryRepository: SelectedCategoriesRepository,
    private val selectedParametersRepository: SelectedParametersRepository,
    private val selectedLotFormRepository: SelectedLotFormRepository,
): UpdateParametersInteractor {
    private val _updateFlow = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1)
    override val updateFlow = _updateFlow.asSharedFlow()

    override fun update() {
        _updateFlow.tryEmit(Unit)
    }

    override suspend fun loadParameters() {
        val currentCategorySelection = selectedCategoryRepository.currentCategorySelection.value

        val innerCategory = currentCategorySelection.innerCategory ?: return

        val categoryId = innerCategory.id
        val lotFormId = selectedLotFormRepository.currentLotForm.value?.id ?: 1

        val parameters = parametersRepository.loadParameters(categoryId, lotFormId)

        selectedParametersRepository.updateParameters(parameters)
    }
}