package ru.zveron.lot_card.data

import ru.zveron.contract.lot.CardLot
import ru.zveron.contract.lot.Seller as GrpcSeller
import ru.zveron.contract.lot.model.CommunicationChannel as GrpcCommunicationChannel
import ru.zveron.lot_card.domain.ChannelLink
import ru.zveron.lot_card.domain.CommunicationChannel
import ru.zveron.lot_card.domain.Contact
import ru.zveron.contract.lot.Contact as GrpcContact
import ru.zveron.lot_card.domain.Gender
import ru.zveron.contract.lot.model.Gender as GrpcGender
import ru.zveron.contract.lot.Statistics as GrpcStatistics
import ru.zveron.contract.lot.model.Parameter as GrpcParameter
import ru.zveron.contract.lot.model.Address as GrpcAddress
import ru.zveron.lot_card.domain.LotInfo
import ru.zveron.lot_card.domain.Parameter
import ru.zveron.lot_card.domain.Seller
import ru.zveron.lot_card.domain.Statistics

fun CardLot.toLotInfo(): LotInfo {
    return LotInfo(
        title = this.title,
        photos = this.photosList.map { it.url },
        gender = this.gender.toDomainGender(),
        address = this.address.toDomainString(),
        parameters = this.parametersList.map { it.toDomainParameter() },
        description = this.description,
        price = this.price,
        statistics = this.statistics.toDomainStatistics(),
        contact = this.contact.toDomainContact(),
        seller = this.seller.toDomainSeller(),
    )
}

private fun GrpcAddress.toDomainString(): String {
    return this.address
}

private fun GrpcParameter.toDomainParameter(): Parameter {
    return Parameter(
        name = this.name,
        value = this.value,
    )
}

private fun GrpcStatistics.toDomainStatistics(): Statistics {
    return Statistics(views = this.view, favorites = this.favorite)
}

private fun GrpcGender.toDomainGender(): Gender {
    return when (this) {
        GrpcGender.FEMALE -> Gender.FEMALE
        GrpcGender.MALE -> Gender.MALE
        GrpcGender.METIS -> Gender.METIS
        GrpcGender.UNRECOGNIZED -> Gender.UNKNOWN
    }
}

private fun GrpcContact.toDomainContact(): Contact {
    return Contact(
        channels = this.communicationChannelList.map { it.toDomainCommunicationChannel() },
        channelLink = ChannelLink(
            phone = this.channelLink.phone,
            vk = this.channelLink.vk,
            email = this.channelLink.email,
        ),
    )
}

private fun GrpcCommunicationChannel.toDomainCommunicationChannel(): CommunicationChannel {
    return when (this) {
        GrpcCommunicationChannel.PHONE -> CommunicationChannel.PHONE
        GrpcCommunicationChannel.VK -> CommunicationChannel.VK
        GrpcCommunicationChannel.EMAIL -> CommunicationChannel.EMAIL
        GrpcCommunicationChannel.CHAT -> CommunicationChannel.CHAT
        GrpcCommunicationChannel.UNRECOGNIZED -> CommunicationChannel.UNKNOWN
    }
}

fun GrpcSeller.toDomainSeller(): Seller {
    return Seller(
        id = this.id,
        name = "${this.name} ${this.surname}",
        avatarUrl = this.imageUrl,
        rating = this.rating,
    )
}