package ru.zveron.categories.data

import ru.zveron.categories.models.Category


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
        val categories = remoteCategorySource.loadChildCategories(categoryId)
        categoryLocalCacheSource.addCategories(categories)
        return categories
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