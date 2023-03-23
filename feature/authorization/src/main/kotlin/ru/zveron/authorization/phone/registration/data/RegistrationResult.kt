package ru.zveron.authorization.phone.registration.data

import ru.zveron.authorization.model.Token

data class RegistrationResult(
    val accessToken: Token,
    val refreshToken: Token,
)