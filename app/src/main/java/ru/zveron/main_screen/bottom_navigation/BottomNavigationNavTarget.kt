package ru.zveron.main_screen.bottom_navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class BottomNavigationNavTarget: Parcelable {
    @Parcelize
    object LotsFeed: BottomNavigationNavTarget()

    @Parcelize
    object Favorites: BottomNavigationNavTarget()

    @Parcelize
    object UserLots: BottomNavigationNavTarget()
}