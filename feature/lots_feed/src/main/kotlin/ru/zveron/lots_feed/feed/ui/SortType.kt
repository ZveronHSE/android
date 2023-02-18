package ru.zveron.lots_feed.feed.ui

import androidx.annotation.StringRes
import ru.zveron.lots_feed.R

enum class SortType(@StringRes val titleRes: Int) {
    DATE(R.string.date_desc_filter),
    PRICE(R.string.price_desc_filter),
}