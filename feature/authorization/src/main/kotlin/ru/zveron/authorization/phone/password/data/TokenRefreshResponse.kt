package ru.zveron.authorization.phone.password.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenRefreshResponse(
    val expiresIn: Int?,
    val refreshExpiresIn: Long?,
)