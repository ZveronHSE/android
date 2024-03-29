package ru.zveron.lots_feed.filters_screen.domain.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.zveron.choose_item.ChooseItem
import ru.zveron.choose_item.ChooseItemItemProvider
import ru.zveron.choose_item.ChooseItemUiState
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.filters_screen.data.parameters.FiltersSelectedParametersRepository
import ru.zveron.parameters.data.ParametersRepository

internal class FiltersParameterItemProvider(
    private val parameterId: Int,
    private val selectedParametersRepository: FiltersSelectedParametersRepository,
    parametersRepository: ParametersRepository,
) : ChooseItemItemProvider {
    private val parameter = parametersRepository.getParameterById(parameterId)

    private val items = parameter.possibleValues.mapIndexed { index, value ->
        ChooseItem(index, ZveronText.RawString(value)) to value
    }

    override val uiState: StateFlow<ChooseItemUiState> =
        MutableStateFlow(ChooseItemUiState.Success(items.map { it.first }))

    override fun itemPicked(id: Int) {
        selectedParametersRepository.setParameterValue(parameterId, items[id].second)
    }
}