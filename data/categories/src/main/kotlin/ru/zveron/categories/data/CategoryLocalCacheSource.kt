package ru.zveron.categories.data

import android.util.Log
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
        Log.d("Category", "adding root categories with size ${rootCategories.size}: ${categories.joinToString()}")
        if(rootCategories.isEmpty()) {
            rootCategories.addAll(categories)
            Log.d("Category", "new root categories with size ${rootCategories.size}: ${rootCategories.joinToString()}")
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