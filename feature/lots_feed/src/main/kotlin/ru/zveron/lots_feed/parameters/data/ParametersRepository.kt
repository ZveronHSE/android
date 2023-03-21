package ru.zveron.lots_feed.parameters.data

import ru.zveron.lots_feed.models.parameters.Parameter

internal class ParametersRepository(
    private val parametersSource: ParametersSource,
    private val parametersLocalCache: ParametersLocalCache,
) {

    /**
     * NOT SAFE TO CALL, USE WITH CAUTION
     */
    fun getParameterById(parameterId: Int): Parameter {
        return parametersLocalCache.getParameterById(parameterId)!!
    }

    suspend fun loadParameters(categoryId: Int, lotFormId: Int): List<Parameter> {
        val parameters = parametersSource
            .loadParameters(categoryId, lotFormId)
            // TODO: remove this when adding support for multiple parameters types
            .filter { it.possibleValues.isNotEmpty() }
        parametersLocalCache.cacheParameters(parameters)
        return parameters
    }
}