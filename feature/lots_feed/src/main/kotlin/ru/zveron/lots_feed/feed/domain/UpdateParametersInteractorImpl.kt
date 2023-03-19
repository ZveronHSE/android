package ru.zveron.lots_feed.feed.domain

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.zveron.lots_feed.categories.data.SelectedCategoriesRepository
import ru.zveron.lots_feed.feed.data.parameters.SelectedParametersRepository
import ru.zveron.lots_feed.parameters.data.ParametersRepository

internal class UpdateParametersInteractorImpl(
    private val parametersRepository: ParametersRepository,
    private val selectedCategoryRepository: SelectedCategoriesRepository,
    // TODO: add selected lot forms repository
    private val selectedParametersRepository: SelectedParametersRepository,
): UpdateParametersInteractor {
    private val _updateFlow = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1)
    override val updateFlow = _updateFlow.asSharedFlow()

    override fun update() {
        _updateFlow.tryEmit(Unit)
    }

    override suspend fun loadParameters() {
        val currentCategorySelection = selectedCategoryRepository.currentCategorySelection.value

        if (currentCategorySelection.innerCategory == null) {
            return
        }

        val categoryId = currentCategorySelection.innerCategory.id
        // TODO: add lot form
        val lotFormId = 1

        val parameters = parametersRepository.loadParameters(categoryId, lotFormId)

        selectedParametersRepository.updateParameters(parameters)
    }
}