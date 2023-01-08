package ru.zveron.authorization.phone

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class RootPhoneNavTarget: Parcelable {
    @Parcelize
    object PhoneInput: RootPhoneNavTarget()

    @Parcelize
    data class SmsCodeInput(val phoneNumber: String): RootPhoneNavTarget()

    @Parcelize
    object PasswordInput: RootPhoneNavTarget()

    @Parcelize
    object Registration: RootPhoneNavTarget()
}