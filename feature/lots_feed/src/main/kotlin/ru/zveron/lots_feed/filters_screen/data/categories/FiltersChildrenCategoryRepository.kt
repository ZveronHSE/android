package ru.zveron.lots_feed.filters_screen.data.categories

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.categories.data.CategoryRepository
import ru.zveron.categories.models.Category

class FiltersChildrenCategoryRepository(
    private val categoryRepository: CategoryRepository,
) {
    private val _childrenCategoryState = MutableStateFlow<ChildCategory>(ChildCategory.Loading)
    val childrenCategoryState = _childrenCategoryState.asStateFlow()

    suspend fun updateChildrenCategory(rootCategoryId: Int) {
        _childrenCategoryState.update { ChildCategory.Loading }
        val categories = categoryRepository.loadCategoryChildren(rootCategoryId)
        _childrenCategoryState.update { ChildCategory.Success(categories) }
    }
}

sealed class ChildCategory {
    object Loading: ChildCategory()

    data class Success(val categories: List<Category>): ChildCategory()
}