package ru.zveron.lots_feed.categories.data

import ru.zveron.lots_feed.models.categories.Category


class CategoryRepository(
    private val remoteCategorySource: CategorySource,
    private val categoryLocalCacheSource: CategoryLocalCacheSource,
) {
    fun getCategoryById(id: Int): Category {
        // UNSAFE USE CAREFULLY
        return categoryLocalCacheSource.getCategoryByID(id)!!
    }

    suspend fun loadCategoryChildren(categoryId: Int): List<Category> {
        // TODO: add local caching here
        return remoteCategorySource.loadChildCategories(categoryId)
    }

    suspend fun loadRootCategories(): List<Category> {
        if (categoryLocalCacheSource.containsRootCategories()) {
            return categoryLocalCacheSource.loadRootCategories()
        }

        val categories = remoteCategorySource.loadRootCategories()
        categoryLocalCacheSource.addRootCategories(categories)

        return categories
    }
}