package ru.zveron.lots_feed.categories.domain

import ru.zveron.lots_feed.categories.data.CategoryRepository
import ru.zveron.lots_feed.categories.data.SelectedCategoriesRepository
import ru.zveron.lots_feed.models.categories.Category

class SelectedCategoriesInteractor(
    private val selectedCategoriesRepository: SelectedCategoriesRepository,
    private val categoryRepository: CategoryRepository,
) {
    val currentCategorySelection = selectedCategoriesRepository.currentCategorySelection

    fun setNextLevelCategory(categoryId: Int) {
        val category = categoryRepository.getCategoryById(categoryId)
        setNextLevelCategory(category)
    }

    fun resetCurrentCategory(): Boolean {
        return selectedCategoriesRepository.resetCurrentCategory()
    }

    fun canResetCurrentCategory(): Boolean {
        return selectedCategoriesRepository.canResetCategory()
    }

    private fun setNextLevelCategory(category: Category) {
        val currentSelection = currentCategorySelection.value

        if (currentSelection.rootCategory == null) {
            selectedCategoriesRepository.setRootCategory(category)
        } else if(currentSelection.innerCategory == null) {
            selectedCategoriesRepository.setInnerCategory(category)
        }
    }
}