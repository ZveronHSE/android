package ru.zveron.lots_feed.feed

import ru.zveron.design.resources.ZveronText

interface LotsFeedNavigator {
    fun goToFilters()

    fun goToSearch()

    fun goToLot(lotId: Long)

    fun chooseItem(title: ZveronText)

    fun startAuthorization()
}