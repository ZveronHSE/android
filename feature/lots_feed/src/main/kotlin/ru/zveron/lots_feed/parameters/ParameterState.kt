package ru.zveron.lots_feed.filters_screen.data.parameters

import ru.zveron.lots_feed.models.parameters.Parameter

data class ParameterState(
    val parameter: Parameter,
    val value: String?,
)