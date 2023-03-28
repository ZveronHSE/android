package ru.zveron.categories.mappings

import ru.zveron.categories.models.Category
import ru.zveron.contract.parameter.external.Category as GrpcCategory

fun GrpcCategory.toDomainCategory(): Category {
    return Category(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl
    )
}