package ru.zveron.parameters.data

import ru.zveron.models.parameters.Parameter

class ParametersLocalCache {
    private val cachedCategories = mutableSetOf<Parameter>()

    fun cacheParameters(parameters: List<Parameter>) {
        cachedCategories.addAll(parameters)
    }

    fun getParameterById(id: Int): Parameter? {
        return cachedCategories.firstOrNull { it.id == id }
    }
}