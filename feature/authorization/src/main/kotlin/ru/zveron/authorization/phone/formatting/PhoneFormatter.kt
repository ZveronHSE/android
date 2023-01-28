package ru.zveron.authorization.phone.formatting

import android.telephony.PhoneNumberUtils

class PhoneFormatter {
    fun formatPhoneToVisual(phone: String): String {
        return PhoneNumberUtils.formatNumber(formatPhoneForUtils(phone), "RU")
    }

    fun formatPhoneInputToRequest(phone: String) : String {
        return "7$phone"
    }

    private fun formatPhoneForUtils(phone: String): String {
        return "+7$phone"
    }
}