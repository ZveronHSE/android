package ru.zveron.lots_feed.filters_screen.data.categories

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.lots_feed.categories.data.CategoryRepository
import ru.zveron.lots_feed.categories.data.CategorySelection

class FiltersSelectedCategoryHolder(
    private val categoryRepository: CategoryRepository,
) {
    private val _currentCategorySelection = MutableStateFlow(CategorySelection.EMPTY)
    val currentCategorySelection = _currentCategorySelection.asStateFlow()

    fun selectRootCategory(id: Int?) {
        if (id == null) {
            resetRootCategory()
            return
        }

        val category = categoryRepository.getCategoryById(id)
        _currentCategorySelection.update {
            if (it.rootCategory == category) {
                it
            } else {
                CategorySelection(category, null)
            }
        }
    }

    private fun resetRootCategory() {
        _currentCategorySelection.update { CategorySelection.EMPTY }
    }

    fun selectCategory(id: Int?) {
        if (id == null) {
            resetInnerCategory()
            return
        }

        val category = categoryRepository.getCategoryById(id)
        _currentCategorySelection.update {
            it.copy(innerCategory = category)
        }
    }

    private fun resetInnerCategory() {
        _currentCategorySelection.update { it.copy(innerCategory = null) }
    }
}