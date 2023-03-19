package ru.zveron.lots_feed.feed.domain.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.zveron.lots_feed.choose_item.ChooseItem
import ru.zveron.lots_feed.choose_item.ChooseItemItemProvider
import ru.zveron.lots_feed.choose_item.ChooseItemUiState
import ru.zveron.lots_feed.feed.data.parameters.SelectedParametersRepository
import ru.zveron.lots_feed.parameters.data.ParametersRepository

internal class ParameterItemProvider(
    private val parameterId: Int,
    private val selectedParametersRepository: SelectedParametersRepository,
    parametersRepository: ParametersRepository,
) : ChooseItemItemProvider {
    private val parameter = parametersRepository.getParameterById(parameterId)

    private val items = parameter.possibleValues.mapIndexed { index, value ->
        ChooseItem(index, value)
    }

    override val uiState: StateFlow<ChooseItemUiState> =
        MutableStateFlow(ChooseItemUiState.Success(items))

    override fun itemPicked(id: Int) {
        selectedParametersRepository.setParameterValue(parameterId, items[id].title)
    }
}