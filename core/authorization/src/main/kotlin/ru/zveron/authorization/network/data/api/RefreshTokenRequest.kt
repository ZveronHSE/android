package ru.zveron.authorization.network.data.api

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RefreshTokenRequest(
    val fingerPrint: String,
)