package ru.zveron.lots_feed.feed.domain.parameters

import ru.zveron.choose_item.ChooseItemItemProvider
import ru.zveron.lots_feed.feed.data.parameters.SelectedParametersRepository
import ru.zveron.lots_feed.feed.domain.UpdateFeedInteractor
import ru.zveron.parameters.data.ParametersRepository

internal class ParameterItemProviderFactory(
    private val selectedParametersRepository: SelectedParametersRepository,
    private val parametersRepository: ParametersRepository,
    private val updateFeedInteractor: UpdateFeedInteractor,
) {
    fun createItemProvider(parameterId: Int): ChooseItemItemProvider {
        return ParameterItemProvider(
            parameterId,
            selectedParametersRepository,
            parametersRepository,
            updateFeedInteractor,
        )
    }
}