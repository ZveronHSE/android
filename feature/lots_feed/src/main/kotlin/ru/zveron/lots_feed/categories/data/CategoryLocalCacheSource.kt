package ru.zveron.lots_feed.categories.data

import ru.zveron.lots_feed.models.categories.Category

class CategoryLocalCacheSource: CategorySource {
    private val rootCategories = mutableListOf<Category>()

    fun getCategoryByID(id: Int): Category? {
        return rootCategories.find { it.id == id }
    }

    fun containsRootCategories(): Boolean {
        return rootCategories.isNotEmpty()
    }

    fun addRootCategories(categories: List<Category>) {
        rootCategories.addAll(categories)
    }

    override suspend fun loadRootCategories(): List<Category> {
        return rootCategories
    }

    /**
     * DO NOT USE THIS IMPLEMENTATION, LOCAL CACHING NOT IMPLEMENTED
     */
    override suspend fun loadChildCategories(categoryId: Int): List<Category> {
        TODO("implement local cache for child categories")
    }
}