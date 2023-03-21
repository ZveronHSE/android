package ru.zveron.lots_feed.parameters.data

import ru.zveron.lots_feed.models.parameters.Parameter

internal class ParametersLocalCache {
    private val cachedCategories = mutableSetOf<Parameter>()

    fun cacheParameters(parameters: List<Parameter>) {
        cachedCategories.addAll(parameters)
    }

    fun getParameterById(id: Int): Parameter? {
        return cachedCategories.firstOrNull { it.id == id }
    }
}