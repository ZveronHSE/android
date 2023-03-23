package ru.zveron.categories.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.categories.models.Category

class SelectedCategoriesRepository {
    private val _currentCategorySelection = MutableStateFlow(CategorySelection(null, null))
    val currentCategorySelection = _currentCategorySelection.asStateFlow()

    fun setRootCategory(rootCategory: Category?) {
        _currentCategorySelection.update {
            if (it.rootCategory == rootCategory) {
                it
            } else {
                CategorySelection(rootCategory, null)
            }
        }
    }

    fun setInnerCategory(innerCategory: Category?) {
        _currentCategorySelection.update {
            it.copy(innerCategory = innerCategory)
        }
    }

    fun resetCurrentCategory(): Boolean {
        if (_currentCategorySelection.value.isEmpty()) {
            return false
        }

        _currentCategorySelection.update {
            if (it.innerCategory != null) {
                it.copy(innerCategory = null)
            } else {
                CategorySelection(null, null)
            }
        }
        return true
    }

    fun canResetCategory(): Boolean {
        return !_currentCategorySelection.value.isEmpty()
    }

    fun setCategorySelection(categorySelection: CategorySelection) {
        _currentCategorySelection.update { categorySelection }
    }
}

data class CategorySelection(
    val rootCategory: Category?,
    val innerCategory: Category?,
) {
    fun getCurrentCategory(): Category? {
        return innerCategory ?: rootCategory
    }

    fun isEmpty(): Boolean {
        return rootCategory == null && innerCategory == null
    }

    companion object {
        val EMPTY = CategorySelection(null, null)
    }
}