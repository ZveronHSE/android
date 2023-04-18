package ru.zveron.main_screen.bottom_navigation.user_lots_backstack

interface UserLotsBackstackNavigator {
    fun createUserLot()

    fun goToLot(id: Long)
}