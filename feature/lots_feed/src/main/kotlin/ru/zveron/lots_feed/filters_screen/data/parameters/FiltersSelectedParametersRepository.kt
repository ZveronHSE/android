package ru.zveron.lots_feed.filters_screen.data.parameters

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.lots_feed.models.parameters.Parameter

internal class FiltersSelectedParametersRepository {
    private var _currentParameters = emptyList<Parameter>()
    private val _currentParametersValues = mutableMapOf<Int, String>()

    private val _parametersState = MutableStateFlow<List<ParameterState>>(emptyList())
    val parametersState = _parametersState.asStateFlow()

    fun updateParameters(parameters: List<Parameter>) {
        _currentParameters = parameters
        updateState()
    }

    fun setParameterValue(id: Int, value: String) {
        _currentParametersValues[id] = value
        updateState()
    }

    fun resetParamters() {
        _currentParameters = emptyList()
        // _currentParametersValues.clear()
    }

    private fun updateState() {
        _parametersState.update {
            _currentParameters.map { parameter ->
                ParameterState(parameter, _currentParametersValues[parameter.id])
            }
        }
    }
}

data class ParameterState(
    val parameter: Parameter,
    val value: String?,
)