package ru.zveron.lots_feed.categories.domain

import ru.zveron.categories.data.CategoryRepository
import ru.zveron.categories.data.SelectedCategoriesRepository
import ru.zveron.categories.models.Category
import ru.zveron.lots_feed.feed.data.parameters.SelectedParametersRepository
import ru.zveron.lots_feed.feed.domain.QueryInteractor
import ru.zveron.lots_feed.feed.domain.UpdateFeedInteractor
import ru.zveron.lots_feed.feed.domain.UpdateParametersInteractor

class SelectedCategoriesInteractor(
    private val selectedCategoriesRepository: SelectedCategoriesRepository,
    private val categoryRepository: CategoryRepository,
    private val updateFeedInteractor: UpdateFeedInteractor,
    private val updateParametersInteractor: UpdateParametersInteractor,
    private val selectedParametersRepository: SelectedParametersRepository,
    private val queryInteractor: QueryInteractor,
) {
    val currentCategorySelection = selectedCategoriesRepository.currentCategorySelection

    fun setNextLevelCategory(categoryId: Int) {
        val category = categoryRepository.getCategoryById(categoryId)
        setNextLevelCategory(category)

        queryInteractor.resetQuery()
        updateFeedInteractor.update()
    }

    fun resetCurrentCategory(): Boolean {
        val success = selectedCategoriesRepository.resetCurrentCategory()
        if (success) {
            selectedParametersRepository.resetParamters()
            queryInteractor.resetQuery()
            updateFeedInteractor.update()
        }
        return success
    }

    private fun setNextLevelCategory(category: Category) {
        val currentSelection = currentCategorySelection.value

        if (currentSelection.rootCategory == null) {
            selectedCategoriesRepository.setRootCategory(category)
            selectedParametersRepository.resetParamters()
        } else if(currentSelection.innerCategory == null) {
            selectedCategoriesRepository.setInnerCategory(category)
            selectedParametersRepository.resetParamters()
            updateParametersInteractor.update()
        }
    }
}