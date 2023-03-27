package ru.zveron.models.mappings

import ru.zveron.models.lots.Lot
import ru.zveron.models.lots.Status
import ru.zveron.contract.core.Status as GrpcStatus
import ru.zveron.contract.core.Lot as GrpcLot

fun GrpcLot.toDomainLot(): Lot {
    return Lot(
        id = this.id,
        title = this.title,
        price = this.price,
        publicationDate = this.publicationDate,
        photoUrl = this.imageUrl,
        isFavorite = this.favorite,
        status = this.status.toDomainStatus(),
        categoryId = if (this.hasCategoryId()) this.categoryId else null,
    )
}

fun GrpcStatus.toDomainStatus(): Status {
    return when (this) {
        GrpcStatus.ACTIVE -> Status.ACTIVE
        GrpcStatus.CLOSED -> Status.CLOSED
        GrpcStatus.CANCELED -> Status.CANCELED
        GrpcStatus.UNRECOGNIZED -> Status.UNKNOWN
    }
}

fun Status.toGrpcStatus(): GrpcStatus {
    return when (this) {
        Status.ACTIVE -> GrpcStatus.ACTIVE
        Status.CLOSED -> GrpcStatus.CLOSED
        Status.CANCELED -> GrpcStatus.CANCELED
        Status.UNKNOWN -> GrpcStatus.UNRECOGNIZED
    }
}