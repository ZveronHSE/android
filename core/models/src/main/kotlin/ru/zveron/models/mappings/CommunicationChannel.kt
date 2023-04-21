package ru.zveron.models.mappings

import ru.zveron.models.communication_channels.CommunicationChannel
import ru.zveron.contract.lot.model.CommunicationChannel as GrpcCommunicationChannel

fun CommunicationChannel.toGrpcCommunicationChannel(): GrpcCommunicationChannel {
    return when (this) {
        CommunicationChannel.CALL -> GrpcCommunicationChannel.PHONE
        CommunicationChannel.CHAT -> GrpcCommunicationChannel.CHAT
    }
}