package ru.zveron.authorization.phone.phone_input.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhoneSendRequest(
    val phone: String,
)