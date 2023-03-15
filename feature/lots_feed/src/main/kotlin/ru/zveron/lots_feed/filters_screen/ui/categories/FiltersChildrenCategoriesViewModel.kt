package ru.zveron.lots_feed.filters_screen.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.choose_item.ChooseItemHolder
import ru.zveron.lots_feed.filters_screen.FiltersNavigator
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryHolder
import ru.zveron.lots_feed.filters_screen.domain.ChildCategoryItemProvider

class FiltersChildrenCategoriesViewModel(
    filtersSelectedCategoryHolder: FiltersSelectedCategoryHolder,
    private val chooseItemHolder: ChooseItemHolder,
    private val childCategoryItemProvider: ChildCategoryItemProvider,
    private val filtersNavigator: FiltersNavigator,
): ViewModel() {
    val uiState = filtersSelectedCategoryHolder.currentCategorySelection.map { categorySelection ->
        if (categorySelection.rootCategory == null) {
            ChildrenCategoriesUiState.Hidden
        } else {
            val text = if (categorySelection.innerCategory == null) {
                ZveronText.RawString(categorySelection.rootCategory.name)
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
}