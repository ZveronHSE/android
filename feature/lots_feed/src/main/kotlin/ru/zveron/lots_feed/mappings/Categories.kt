package ru.zveron.lots_feed.mappings

import ru.zveron.categories.models.Category
import ru.zveron.design.resources.ZveronImage
import ru.zveron.lots_feed.categories.ui.CategoryUiState

fun Category.toUiState(): CategoryUiState {
    return CategoryUiState(
        id = this.id,
        image = ZveronImage.RemoteImage(this.imageUrl),
        title = this.name,
    )
}