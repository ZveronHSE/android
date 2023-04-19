package ru.zveron.parameters.data

import ru.zveron.models.parameters.Parameter

internal class ParametersRepositoryImpl(
    private val parametersSource: ParametersSource,
    private val parametersLocalCache: ParametersLocalCache,
): ParametersRepository {

    /**
     * NOT SAFE TO CALL, USE WITH CAUTION
     */
    override fun getParameterById(parameterId: Int): Parameter {
        return parametersLocalCache.getParameterById(parameterId)!!
    }

    override suspend fun loadParameters(categoryId: Int, lotFormId: Int): List<Parameter> {
        val parameters = parametersSource
            .loadParameters(categoryId, lotFormId)
            // TODO: remove this when adding support for multiple parameters types
            .filter { it.possibleValues.isNotEmpty() }
        parametersLocalCache.cacheParameters(parameters)
        return parameters
    }
}