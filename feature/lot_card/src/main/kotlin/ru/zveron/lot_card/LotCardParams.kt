package ru.zveron.lot_card

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LotCardParams(
    val id: Long,
): Parcelable