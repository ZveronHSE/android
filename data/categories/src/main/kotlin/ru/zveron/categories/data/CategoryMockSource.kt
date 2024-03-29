package ru.zveron.categories.data

import kotlinx.coroutines.delay
import ru.zveron.categories.models.Category

class CategoryMockSource: CategorySource {
    override suspend fun loadRootCategories(): List<Category> {
        delay(1000L)

        return listOf(
            Category(
                id = 1,
                name = "Животные",
                imageUrl = "https://img.icons8.com/3d-fluency/256/dog.png",
            ),
            Category(
                id = 2,
                name = "Товары",
                imageUrl = "https://img.icons8.com/3d-fluency/256/dog-house.png",
            )
        )
    }

    override suspend fun loadChildCategories(categoryId: Int): List<Category> {
        delay(1000L)

        return listOf(
            Category(
                id = 3,
                name = "Собаки",
                imageUrl = "https://img.icons8.com/fluency/256/poodle.png",
            ),
            Category(
                id = 4,
                name = "Коты",
                imageUrl = "https://img.icons8.com/fluency/256/cat.png",
            )
        )
    }
}