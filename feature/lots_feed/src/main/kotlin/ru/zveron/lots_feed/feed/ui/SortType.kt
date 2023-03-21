package ru.zveron.lots_feed.feed.ui

import androidx.annotation.StringRes
import ru.zveron.lots_feed.R

enum class SortType(@StringRes val titleRes: Int) {
    DATE(R.string.date_desc_filter),
    CHEAP(R.string.cheap_desc_filter),
    EXPENSIVE(R.string.expensive_desc_filter),
//    HIGH_RATING(R.string.high_rating_desc_filter),
}