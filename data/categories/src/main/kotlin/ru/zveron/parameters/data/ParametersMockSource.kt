package ru.zveron.parameters.data

import kotlinx.coroutines.delay
import ru.zveron.models.parameters.Parameter
import ru.zveron.models.parameters.ParameterType

internal class ParametersMockSource: ParametersSource {
    override suspend fun loadParameters(categoryId: Int, lotFormId: Int): List<Parameter> {
        delay(1000L)
        return listOf(
            Parameter(
                id = 1,
                name = "Порода",
                type = ParameterType.STRING,
                isRequired = false,
                possibleValues = listOf("Хороший мальчик", "Очень хороший мальчик")
            ),
            Parameter(
                id = 2,
                name = "Окрас",
                type = ParameterType.STRING,
                isRequired = false,
                possibleValues = listOf("Красивый", "Очень красивый")
            ),
        )
    }
}