package ru.zveron.lots_feed.categories.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.zveron.categories.data.CategoryRepository
import ru.zveron.categories.models.Category

internal class CategoryInteractor(
    private val categoryRepository: CategoryRepository,
) {
    suspend fun loadChildrenCategories(categoryId: Int?): List<Category> {
        return withContext(Dispatchers.IO) {
            if (categoryId == null) {
                categoryRepository.loadRootCategories()
            } else {
                categoryRepository.loadCategoryChildren(categoryId)
            }
        }
    }
}