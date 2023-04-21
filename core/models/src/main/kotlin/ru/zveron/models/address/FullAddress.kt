package ru.zveron.models.address

data class FullAddress(
    val latitude: Double,
    val longitude: Double,
    val title: String?,
    val region: String?,
    val district: String?,
    val town: String?,
    val street: String?,
    val house: String?,
)