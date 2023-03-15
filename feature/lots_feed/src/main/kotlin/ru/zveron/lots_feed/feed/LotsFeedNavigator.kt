package ru.zveron.lots_feed.feed

interface LotsFeedNavigator {
    fun goToFilters()

    fun goToSearch()

    fun goToLot(lotId: Long)
}