package ru.zveron.parameters.data

import ru.zveron.models.parameters.Parameter

interface ParametersRepository {
    fun getParameterById(parameterId: Int): Parameter

    suspend fun loadParameters(categoryId: Int, lotFormId: Int): List<Parameter>
}