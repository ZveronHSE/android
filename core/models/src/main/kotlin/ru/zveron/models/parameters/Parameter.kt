package ru.zveron.models.parameters

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