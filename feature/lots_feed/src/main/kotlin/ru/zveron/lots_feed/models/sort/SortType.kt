package ru.zveron.lots_feed.models.sort

sealed class SortType {
    object Newest: SortType()

    object Cheap: SortType()

    object Expensive: SortType()
}