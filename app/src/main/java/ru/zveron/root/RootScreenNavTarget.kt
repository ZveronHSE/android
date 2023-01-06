package ru.zveron.root

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class RootScreenNavTarget: Parcelable {
    @Parcelize
    object MainPage: RootScreenNavTarget()

    @Parcelize
    object AuthorizationBottomSheet: RootScreenNavTarget()
}