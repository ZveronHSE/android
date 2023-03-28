package ru.zveron.categories.data

import ru.zveron.categories.models.Category

interface CategorySource {
    suspend fun loadRootCategories(): List<Category>

    suspend fun loadChildCategories(categoryId: Int): List<Category>
}