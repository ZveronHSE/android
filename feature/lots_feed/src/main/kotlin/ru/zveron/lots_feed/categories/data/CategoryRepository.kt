package ru.zveron.lots_feed.categories.data

import ru.zveron.lots_feed.models.categories.Category


class CategoryRepository(
    private val remoteCategorySource: CategorySource,
) {
    suspend fun loadCategoryChildren(categoryId: Int): List<Category> {
        // TODO: add local caching here
        return remoteCategorySource.loadChildCategories(categoryId)
    }

    suspend fun loadRootCategories(): List<Category> {
        // TODO: add local caching here
        return remoteCategorySource.loadRootCategories()
    }
}