package ru.zveron.lots_feed.filters_screen.data.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.lots_feed.models.parameters.Parameter

internal class FiltersSelectedParametersHolder {
    private val _currentParameters = MutableStateFlow<List<Parameter>>(emptyList())
    private val _currentParametersValues = MutableStateFlow<Map<Int, String>>(emptyMap())

    fun updateParameters(parameters: List<Parameter>) {
        _currentParametersValues.update {
            parameters
                .map { it.id }
                .associateWith { "" }
        }
        _currentParameters.update { parameters }
    }

    fun getParameters(): List<Pair<Parameter, String?>> {
        return _currentParameters.value.map { it to getParameterValue(it.id) }
    }

    fun getParameterValue(id: Int): String? {
        return _currentParametersValues.value[id]
    }

    fun setParameterValue(id: Int, value: String) {
        _currentParametersValues.update { currentMap ->
            buildMap {
                this.putAll(currentMap)
                put(id, value)
            }
        }
    }
}