package ru.zveron.lots_feed.mappings

import androidx.compose.runtime.mutableStateOf
import ru.zveron.design.resources.ZveronImage
import ru.zveron.lots_feed.feed.ui.LotUiState
import ru.zveron.models.lots.Lot as DomainLot

fun DomainLot.toUiLot(): LotUiState {
    return LotUiState(
        id = this.id,
        title = this.title,
        price = this.price,
        date = this.publicationDate,
        image = ZveronImage.RemoteImage(this.photoUrl),
        isLiked = mutableStateOf(this.isFavorite),
    )
}