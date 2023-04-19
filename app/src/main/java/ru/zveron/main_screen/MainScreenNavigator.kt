package ru.zveron.main_screen

import ru.zveron.design.resources.ZveronText

interface MainScreenNavigator {
    fun openAuthorization()

    fun openLot(id: Long)

    fun createLot()

    fun pickItem(title: ZveronText)
}