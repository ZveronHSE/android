package ru.zveron.authorization.phone.sms_code.deps

data class SmsCodeDeps(
    val phoneNumber: String,
    val navigator: SmsCodeNavigator,
)