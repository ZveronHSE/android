package ru.zveron.lots_feed.mappings

import ru.zveron.categories.models.Category
import ru.zveron.design.resources.ZveronImage
import ru.zveron.lots_feed.categories.ui.CategoryUiState
import ru.zveron.design.R as DesignR

private const val ANIMALS_CATEGORY_ID = 1
private const val GOODS_CATEGORY_ID = 2

private const val DOG_CATEGORY_ID = 3
private const val CAT_CATEGORY_ID = 4
private const val FISH_CATEGORY_ID = 5
private const val BIRD_CATEGORY_ID = 12

fun Category.toUiState(): CategoryUiState {
    return CategoryUiState(
        id = this.id,
        image = getImage(),
        title = this.name,
    )
}

private fun Category.getImage(): ZveronImage {
    return when (this.id) {
        ANIMALS_CATEGORY_ID -> ZveronImage.ResourceImage(DesignR.drawable.ic_animal_category)
        GOODS_CATEGORY_ID -> ZveronImage.ResourceImage(DesignR.drawable.ic_goods_category)

        DOG_CATEGORY_ID -> ZveronImage.ResourceImage(DesignR.drawable.ic_dog_category)
        CAT_CATEGORY_ID -> ZveronImage.ResourceImage(DesignR.drawable.ic_cat_category)
        FISH_CATEGORY_ID -> ZveronImage.ResourceImage(DesignR.drawable.ic_fish_category)
        BIRD_CATEGORY_ID -> ZveronImage.ResourceImage(DesignR.drawable.ic_bird_category)
        else -> ZveronImage.RemoteImage(this.imageUrl)
    }
}