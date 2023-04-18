package ru.zveron.root

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class RootScreenNavTarget: Parcelable {
    @Parcelize
    object MainPage: RootScreenNavTarget()

    @Parcelize
    object AuthorizationBottomSheet: RootScreenNavTarget()

    @Parcelize
    object PhoneAuthorization: RootScreenNavTarget()

    @Parcelize
    data class LotCard(val id: Long): RootScreenNavTarget()

    @Parcelize
    object CreateLot: RootScreenNavTarget()
}