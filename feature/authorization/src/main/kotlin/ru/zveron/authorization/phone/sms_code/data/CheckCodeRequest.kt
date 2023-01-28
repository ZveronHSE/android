package ru.zveron.authorization.phone.sms_code.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckCodeRequest(
    val phone: String,
    val code: String,
    val fingerprint: String,
)