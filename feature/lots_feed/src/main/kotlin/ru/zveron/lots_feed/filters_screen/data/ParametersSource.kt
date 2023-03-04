package ru.zveron.lots_feed.filters_screen.data

import ru.zveron.lots_feed.models.parameters.Parameter

interface ParametersSource {
    suspend fun loadParameters(categoryId: Int, lotFormId: Int): List<Parameter>
}