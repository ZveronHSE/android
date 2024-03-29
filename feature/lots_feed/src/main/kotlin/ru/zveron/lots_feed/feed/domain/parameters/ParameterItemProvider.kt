package ru.zveron.lots_feed.feed.domain.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.zveron.choose_item.ChooseItem
import ru.zveron.choose_item.ChooseItemItemProvider
import ru.zveron.choose_item.ChooseItemUiState
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.feed.data.parameters.SelectedParametersRepository
import ru.zveron.lots_feed.feed.domain.UpdateFeedInteractor
import ru.zveron.parameters.data.ParametersRepository

internal class ParameterItemProvider(
    private val parameterId: Int,
    private val selectedParametersRepository: SelectedParametersRepository,
    parametersRepository: ParametersRepository,
    private val updateFeedInteractor: UpdateFeedInteractor,
) : ChooseItemItemProvider {
    private val parameter = parametersRepository.getParameterById(parameterId)

    private val items = parameter.possibleValues.mapIndexed { index, value ->
        ChooseItem(index, ZveronText.RawString(value)) to value
    }

    override val uiState: StateFlow<ChooseItemUiState> =
        MutableStateFlow(ChooseItemUiState.Success(items.map { it.first }))

    override fun itemPicked(id: Int) {
        selectedParametersRepository.setParameterValue(parameterId, items[id].second)
        updateFeedInteractor.update()
    }
}