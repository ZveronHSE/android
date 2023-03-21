package ru.zveron.lots_feed.filters_screen.domain.categories

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.zveron.lots_feed.categories.data.CategoryRepository
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersChildrenCategoryRepository
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.models.categories.Category

class FiltersUpdateCategoriesInteractor(
    private val categoryRepository: CategoryRepository,
    private val filtersChildrenCategoryRepository: FiltersChildrenCategoryRepository,
    private val filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
) {
    /**
     * Explanations on replay and extrabuffercapacity parameters
     *
     * This flow will be emmited as a result of calling update method from lots feed screen,
     * so replay needs to be set to be emitted when filters screen is attached.
     * Extra buffer is needed so lots feed screen wont be buffer overflowed
     */
    private val _updateRootCategoriesFlow = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1)
    val updateRootCategoriesFlow = _updateRootCategoriesFlow.asSharedFlow()

    private val _updateChildCategoriesFlow = MutableSharedFlow<Unit>(replay = 1, extraBufferCapacity = 1)
    val updateChildCategoriesFlow = _updateChildCategoriesFlow.asSharedFlow()

    fun updateRootCategories() {
        _updateRootCategoriesFlow.tryEmit(Unit)
    }

    suspend fun loadRootCategories(): List<Category> {
        return categoryRepository.loadRootCategories()
    }

    fun updateChildCategories() {
        _updateChildCategoriesFlow.tryEmit(Unit)
    }

    suspend fun loadChildCategories() {
        val categorySelection = filtersSelectedCategoryRepository.currentCategorySelection.value
        val rootCategoryId = categorySelection.rootCategory?.id ?: return
        filtersChildrenCategoryRepository.updateChildrenCategory(rootCategoryId)
    }
}