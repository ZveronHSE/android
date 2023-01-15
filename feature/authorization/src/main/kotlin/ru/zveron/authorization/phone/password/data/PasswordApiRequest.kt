package ru.zveron.authorization.phone.password.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordApiRequest(
    val phone: String,
    val password: String,
    val fingerPrint: String,
)