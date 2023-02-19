package ru.zveron.lots_feed.categories.data

import ru.zveron.lots_feed.models.categories.Category

interface CategorySource {
    suspend fun loadRootCategories(): List<Category>

    suspend fun loadChildCategories(categoryId: Int): List<Category>
}