package ru.zveron.lots_feed.models.parameters

data class Parameter(
    val id: Int,
    val name: String,
    val type: ParameterType,
    val isRequired: Boolean,
    val possibleValues: List<String>
)

enum class ParameterType {
    STRING,
    DATE,
    INTEGER,
    UNKNOWN
}