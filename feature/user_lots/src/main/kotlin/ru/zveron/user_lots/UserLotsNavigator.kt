package ru.zveron.user_lots

interface UserLotsNavigator {
    fun createLot()

    fun openLot(id: Long)
}