package ru.zveron.lots_feed.mappings

import ru.zveron.design.resources.ZveronImage
import ru.zveron.lots_feed.categories.ui.CategoryUiState
import ru.zveron.lots_feed.models.categories.Category
import ru.zveron.contract.parameter.external.Category as GrpcCategory

fun GrpcCategory.toDomainCategory(): Category {
    return Category(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl
    )
}

fun Category.toUiState(): CategoryUiState {
    return CategoryUiState(
        id = this.id,
        image = ZveronImage.RemoteImage(this.imageUrl),
        title = this.name,
    )
}