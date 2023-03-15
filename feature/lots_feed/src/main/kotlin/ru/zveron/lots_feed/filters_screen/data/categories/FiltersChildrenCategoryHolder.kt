package ru.zveron.lots_feed.filters_screen.data.categories

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.lots_feed.categories.data.CategoryRepository
import ru.zveron.lots_feed.models.categories.Category

class FiltersChildrenCategoryHolder(
    private val categoryRepository: CategoryRepository,
) {
    private val _childrenCategoryState = MutableStateFlow<ChildCategory>(ChildCategory.Loading)
    val childrenCategoryState = _childrenCategoryState.asStateFlow()

    suspend fun updateChildrenCategory(rootCategoryId: Int) {
        try {
            _childrenCategoryState.update { ChildCategory.Loading }
            val categories = categoryRepository.loadCategoryChildren(rootCategoryId)
            _childrenCategoryState.update { ChildCategory.Success(categories) }
        } catch (e: Exception) {
            Log.e("Category", "Error loading children categories", e)
        }
    }
}

sealed class ChildCategory {
    object Loading: ChildCategory()

    data class Success(val categories: List<Category>): ChildCategory()
}