package ru.zveron.lots_feed.models.filters

enum class FilterOperation {
    EQUALITY,
    NEGATION,
    GREATER_THAN,
    GREATER_THAN_EQUALITY,
    LESS_THAN,
    LESS_THAN_EQUALITY,
    IN,
    NOT_IN,
}