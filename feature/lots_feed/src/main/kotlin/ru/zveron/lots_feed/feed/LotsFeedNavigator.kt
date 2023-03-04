package ru.zveron.lots_feed.feed

interface LotsFeedNavigator {
    fun goToFilters(categoryId: Int)

    fun goToCategory(category: CategoryArgument)

    fun goToSearch()

    fun goToLot(lotId: Long)
}