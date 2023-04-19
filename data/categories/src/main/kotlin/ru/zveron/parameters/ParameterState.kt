package ru.zveron.parameters

import ru.zveron.models.parameters.Parameter

data class ParameterState(
    val parameter: Parameter,
    val value: String?,
)