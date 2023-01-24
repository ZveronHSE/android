package ru.zveron.authorization.network.data

data class RefreshTokenResult(
    val newAccessToken: String,
    val newRefreshToken: String,

    val accessTokenExpiresIn: Int?,
    val refreshTokenExpiresIn: Long?,
)
