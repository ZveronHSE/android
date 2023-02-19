package ru.zveron.lots_feed.feed

interface LotsFeedNavigator {
    fun goToFilters()

    fun goToCategory(category: CategoryArgument)

    fun goToSearch()

    fun goToLot(lotId: Long)
}