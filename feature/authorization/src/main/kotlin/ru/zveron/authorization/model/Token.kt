package ru.zveron.authorization.model

data class Token(
    val value: String,
    val expiresIn: Long,
)