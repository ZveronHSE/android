package ru.zveron.authorization.phone.sms_code.deps

interface SmsCodeNavigator {
    fun navigateToPassword()

    fun navigateToRegistration(sessionId: String)
}