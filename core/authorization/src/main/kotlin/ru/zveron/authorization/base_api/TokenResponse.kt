package ru.zveron.authorization.base_api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponse(
    val expiresIn: Int?,
    val refreshExpiresIn: Long?,
)