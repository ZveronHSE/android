package ru.zveron.create_lot.details_step.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.zveron.choose_item.ChooseItem
import ru.zveron.choose_item.ChooseItemItemProvider
import ru.zveron.choose_item.ChooseItemUiState
import ru.zveron.create_lot.data.LotCreateInfoRepository
import ru.zveron.design.resources.ZveronText
import ru.zveron.parameters.data.ParametersRepository

internal class ParametersItemProvider(
    private val parameterId: Int,
    private val parametersRepository: ParametersRepository,
    private val lotCreateInfoRepository: LotCreateInfoRepository,
) : ChooseItemItemProvider {
    private val parameterValuesList by lazy {
        val parameter = parametersRepository.getParameterById(parameterId)
        parameter.possibleValues.mapIndexed { index, value ->
            ChooseItem(
                index,
                ZveronText.RawString(value)
            ) to value
        }
    }

    private val parametersMap by lazy {
        parameterValuesList.associate { it.first.id to it.second }
    }

    override val uiState: StateFlow<ChooseItemUiState> by lazy {
        MutableStateFlow(ChooseItemUiState.Success(parameterValuesList.map { it.first })).asStateFlow()
    }

    override fun itemPicked(id: Int) {
        val value = parametersMap.getValue(id)
        lotCreateInfoRepository.updateParameterValue(parameterId, value)
    }
}