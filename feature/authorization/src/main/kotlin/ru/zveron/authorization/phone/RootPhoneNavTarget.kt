package ru.zveron.authorization.phone

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class RootPhoneNavTarget: Parcelable {
    @Parcelize
    object PhoneInput: RootPhoneNavTarget()

    @Parcelize
    data class SmsCodeInput(val phoneNumber: String, val sessionId: String): RootPhoneNavTarget()

    @Parcelize
    object PasswordInput: RootPhoneNavTarget()

    @Parcelize
    data class Registration(val sessionId: String): RootPhoneNavTarget()
}