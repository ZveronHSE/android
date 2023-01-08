package ru.zveron.authorization.phone

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class RootPhoneNavTarget: Parcelable {
    @Parcelize
    object PhoneInput: RootPhoneNavTarget()
}