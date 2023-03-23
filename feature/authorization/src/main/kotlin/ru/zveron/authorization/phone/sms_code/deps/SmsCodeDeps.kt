package ru.zveron.authorization.phone.sms_code.deps

data class SmsCodeDeps(
    val phoneNumber: String,
    val sessionId: String,
    val navigator: SmsCodeNavigator,
)