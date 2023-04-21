package ru.zveron.create_lot.details_step.domain

import ru.zveron.choose_item.ChooseItemItemProvider
import ru.zveron.create_lot.data.LotCreateInfoRepository
import ru.zveron.parameters.data.ParametersRepository

internal class ParametersItemProviderFactory(
    private val parametersRepository: ParametersRepository,
    private val lotCreateInfoRepository: LotCreateInfoRepository,
) {
    fun createItemProvider(parameterId: Int): ChooseItemItemProvider {
        return ParametersItemProvider(parameterId, parametersRepository, lotCreateInfoRepository)
    }
}