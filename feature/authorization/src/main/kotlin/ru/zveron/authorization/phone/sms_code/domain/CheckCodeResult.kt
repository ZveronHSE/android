package ru.zveron.authorization.phone.sms_code.domain

sealed class CheckCodeResult {
    object Ready: CheckCodeResult()

    data class NeedRegister(val sessionId: String): CheckCodeResult()
}

