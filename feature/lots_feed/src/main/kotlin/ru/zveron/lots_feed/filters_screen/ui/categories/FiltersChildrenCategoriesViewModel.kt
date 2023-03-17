package ru.zveron.lots_feed.filters_screen.ui.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.R
import ru.zveron.lots_feed.choose_item.ChooseItemHolder
import ru.zveron.lots_feed.filters_screen.FiltersNavigator
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryRepository
import ru.zveron.lots_feed.filters_screen.domain.categories.ChildCategoryItemProvider
import ru.zveron.lots_feed.filters_screen.domain.categories.FiltersUpdateCategoriesInteractor

internal class FiltersChildrenCategoriesViewModel(
    filtersSelectedCategoryRepository: FiltersSelectedCategoryRepository,
    private val chooseItemHolder: ChooseItemHolder,
    private val childCategoryItemProvider: ChildCategoryItemProvider,
    private val filtersNavigator: FiltersNavigator,
    private val filtersUpdateCategoriesInteractor: FiltersUpdateCategoriesInteractor,
): ViewModel() {
    init {
        filtersUpdateCategoriesInteractor.updateChildCategoriesFlow
            .onEach { getChildrenCategories() }
            .launchIn(viewModelScope)
    }

    val uiState = filtersSelectedCategoryRepository.currentCategorySelection.map { categorySelection ->
        if (categorySelection.rootCategory == null) {
            ChildrenCategoriesUiState.Hidden
        } else {
            val text = if (categorySelection.innerCategory == null) {
                ZveronText.RawResource(R.string.child_category_selector_title)
            } else {
                ZveronText.RawString(categorySelection.innerCategory.name)
            }
            ChildrenCategoriesUiState.Show(text)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ChildrenCategoriesUiState.Hidden)

    fun childCategoryClicked() {
        val title = getTitle() ?: return
        chooseItemHolder.setCurrentItemItemProvider(childCategoryItemProvider)

        filtersNavigator.chooseItem(title)
    }

    private fun getTitle(): ZveronText? {
        val state = uiState.value as? ChildrenCategoriesUiState.Show ?: return null
        return state.selectedChildCategoryTitle
    }

    private fun getChildrenCategories() {
        viewModelScope.launch {
            try {
                filtersUpdateCategoriesInteractor.loadChildCategories()
            } catch (e: Exception) {
                Log.e("Child categories", "Error loading child categories", e)
            }
        }
    }
}