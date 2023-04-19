package ru.zveron.parameters.data

import ru.zveron.models.parameters.Parameter

interface ParametersSource {
    suspend fun loadParameters(categoryId: Int, lotFormId: Int): List<Parameter>
}