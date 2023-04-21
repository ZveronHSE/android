package ru.zveron.user_profile.data

import ru.zveron.contract.profile.GetProfilePageResponse
import ru.zveron.models.lots.Lot
import ru.zveron.models.mappings.toDomainLot

data class UserProfile(
    val id: Long,
    val name: String,
    val surname: String,
    val avatarUrl: String,
    val address: String,
    // TODO: add contacts
    val rating: Double,
    val reviewAmount: Int,
    val activeLots: List<Lot>,
    val closedLots: List<Lot>,
)

fun GetProfilePageResponse.toUserProfile(): UserProfile {
    return UserProfile(
        id = this.id,
        name = this.name,
        surname = this.surname,
        avatarUrl = this.imageUrl,
        address = this.address,
        rating = this.rating,
        reviewAmount = this.reviewNumber,
        activeLots = this.activeLotsList.map { it.toDomainLot() },
        closedLots = this.closedLotsList.map { it.toDomainLot() },
    )
}
