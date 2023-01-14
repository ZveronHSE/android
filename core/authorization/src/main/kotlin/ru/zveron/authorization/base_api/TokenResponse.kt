package ru.zveron.authorization.base_api

data class TokenResponse(
    val expiresIn: Int?,
    val refreshExpiresIn: Long?,
)