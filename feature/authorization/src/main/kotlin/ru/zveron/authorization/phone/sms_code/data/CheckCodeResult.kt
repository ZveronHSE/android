package ru.zveron.authorization.phone.sms_code.data

import ru.zveron.authorization.model.Token

sealed class CheckCodeResult {
    data class Ready(val accessToken: Token, val refreshToken: Token): CheckCodeResult()

    data class NeedRegister(val sessionId: String): CheckCodeResult()
}

