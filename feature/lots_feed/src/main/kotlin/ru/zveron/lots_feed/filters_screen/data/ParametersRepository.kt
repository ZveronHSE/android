package ru.zveron.lots_feed.filters_screen.data

import ru.zveron.lots_feed.models.parameters.Parameter

internal class ParametersRepository(
    private val parametersSource: ParametersSource,
) {
    suspend fun loadParameters(categoryId: Int, lotFormId: Int): List<Parameter> {
        return parametersSource.loadParameters(categoryId, lotFormId)
    }
}