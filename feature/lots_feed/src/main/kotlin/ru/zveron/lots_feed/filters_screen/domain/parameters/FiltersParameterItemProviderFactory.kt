package ru.zveron.lots_feed.filters_screen.domain.parameters

import ru.zveron.lots_feed.choose_item.ChooseItemItemProvider
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersRepository
import ru.zveron.lots_feed.parameters.data.ParametersRepository

internal class FiltersParameterItemProviderFactory(
    private val selectedParametersRepository: FiltersSelectedParametersRepository,
    private val parametersRepository: ParametersRepository,
) {
    fun createItemProvider(parameterId: Int): ChooseItemItemProvider {
        return FiltersParameterItemProvider(
            parameterId,
            selectedParametersRepository,
            parametersRepository,
        )
    }
}