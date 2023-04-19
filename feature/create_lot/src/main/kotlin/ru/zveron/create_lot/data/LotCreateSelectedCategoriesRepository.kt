package ru.zveron.create_lot.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.categories.data.CategoryRepository
import ru.zveron.categories.data.CategorySelection

internal class LotCreateSelectedCategoriesRepository(
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