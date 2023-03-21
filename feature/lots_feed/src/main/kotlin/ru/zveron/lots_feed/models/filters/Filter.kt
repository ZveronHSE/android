package ru.zveron.lots_feed.models.filters

data class Filter(
    val field: FilterField,
    val operation: FilterOperation,
    val value: String,
)