package ru.zveron.lots_feed.mappings

import ru.zveron.contract.core.Lot
import ru.zveron.design.R
import ru.zveron.design.resources.ZveronImage
import ru.zveron.lots_feed.feed.ui.LotUiState
import ru.zveron.lots_feed.models.waterfall.Lot as DomainLot

fun Lot.toDomainLot(): DomainLot {
    return DomainLot(
        id = this.id,
        title = this.title,
        price = this.price,
        publicationDate = this.publicationDate,
        photoId = this.photoId,
        isFavorite = this.favorite,
    )
}

fun DomainLot.toUiLot(): LotUiState {
    return LotUiState(
        id = this.id,
        title = this.title,
        price = this.price,
        date = this.publicationDate,
//        image = ZveronImage.ResourceImage(R.drawable.cool_dog),
        image = ZveronImage.RemoteImage("https://storage.yandexcloud.net/zveron-order-test/236cd2f1-341a-4a65-8dde-753ecd751a76.jpeg"),
    )
}