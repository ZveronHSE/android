package ru.zveron.categories.data

import ru.zveron.categories.models.Category

class CategoryLocalCacheSource: CategorySource {
    private val rootCategories = mutableListOf<Category>()
    private val categoriesCache = mutableMapOf<Int, Category>()

    fun getCategoryByID(id: Int): Category? {
        return categoriesCache[id]
    }

    fun containsRootCategories(): Boolean {
        return rootCategories.isNotEmpty()
    }

    fun addRootCategories(categories: List<Category>) {
        if(rootCategories.isEmpty()) {
            rootCategories.addAll(categories)
        }
        categoriesCache.putAll(categories.associateBy { it.id })
    }

    fun addCategories(categories: List<Category>) {
        categoriesCache.putAll(categories.associateBy { it.id })
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