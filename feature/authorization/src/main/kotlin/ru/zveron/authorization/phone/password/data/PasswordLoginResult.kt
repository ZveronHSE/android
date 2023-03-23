package ru.zveron.authorization.phone.password.data

import ru.zveron.authorization.model.Token

data class PasswordLoginResult(
    val accessToken: Token,
    val refreshToken: Token,
)