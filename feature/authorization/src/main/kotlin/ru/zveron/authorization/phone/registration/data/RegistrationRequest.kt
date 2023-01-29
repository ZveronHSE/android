package ru.zveron.authorization.phone.registration.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class RegistrationRequest(
    val phone: String,
    val password: String,
    val name: String,
    val surname: String,
    val fingerPrint: String,
)