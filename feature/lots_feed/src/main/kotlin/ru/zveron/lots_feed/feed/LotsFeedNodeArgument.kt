package ru.zveron.lots_feed.feed

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LotsFeedNodeArgument(
    val categoryArgument: CategoryArgument?,
): Parcelable

@Parcelize
data class CategoryArgument(
    val id: Int,
    val title: String,
): Parcelable