package ru.zveron.main_screen.bottom_navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.zveron.R

enum class BottomNavigationItem(
    @DrawableRes val unselectedRes: Int,
    @DrawableRes val selectedRes: Int,
    @StringRes val labelRes: Int,
) {
    LOTS_FEED(R.drawable.home_unselected, R.drawable.home_selected, R.string.lots_feed_label),
    FAVORITES(R.drawable.favourites_unselected, R.drawable.favourites_selected, R.string.favorites_label),
    CREATE_LOT(R.drawable.add_lot_unselected, R.drawable.add_lot_selected, R.string.create_lot_label),
    CHATS(R.drawable.chat_unselected, R.drawable.chat_selected, R.string.messages_label),
    PROFILE(R.drawable.profile_unselected, R.drawable.profile_selected, R.string.profile_label),
}