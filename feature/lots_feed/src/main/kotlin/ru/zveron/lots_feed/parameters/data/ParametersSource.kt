package ru.zveron.lots_feed.parameters.data

import ru.zveron.lots_feed.models.parameters.Parameter

interface ParametersSource {
    suspend fun loadParameters(categoryId: Int, lotFormId: Int): List<Parameter>
}