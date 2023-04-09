package ru.zveron.lot_card.domain

data class LotInfo(
    val title: String,
    val photos: List<String>,
    val gender: Gender,
    val address: String,
    val parameters: List<Parameter>,
    val description: String,
    val price: String,
    val statistics: Statistics,
    val contact: Contact,
    val seller: Seller,
)

enum class Gender {
    MALE,
    FEMALE,
    METIS,
    UNKNOWN,
}

data class Parameter(
    val name: String,
    val value: String,
)

data class Seller(
    val name: String,
    val avatarUrl: String,
    val rating: Double,
)

data class Statistics(
    val views: Int,
    val favorites: Int,
)

data class Contact(
    val channels: List<CommunicationChannel>,
    val channelLink: ChannelLink,
)

enum class CommunicationChannel {
    PHONE,
    VK,
    EMAIL,
    CHAT,
    UNKNOWN,
}

data class ChannelLink(
    val phone: String?,
    val vk: String?,
    val email: String?,
)