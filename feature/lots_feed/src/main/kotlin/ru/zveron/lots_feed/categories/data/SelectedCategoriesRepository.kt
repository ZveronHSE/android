package ru.zveron.lots_feed.categories.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.lots_feed.models.categories.Category

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
}