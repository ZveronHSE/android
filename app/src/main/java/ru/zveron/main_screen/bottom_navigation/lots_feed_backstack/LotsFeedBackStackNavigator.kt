package ru.zveron.main_screen.bottom_navigation.lots_feed_backstack

import ru.zveron.design.resources.ZveronText

interface LotsFeedBackStackNavigator {
    fun goToAuthorization()

    fun goToLot(id: Long)

    fun pickItem(title: ZveronText)

    fun goToSeller(id: Long)
}