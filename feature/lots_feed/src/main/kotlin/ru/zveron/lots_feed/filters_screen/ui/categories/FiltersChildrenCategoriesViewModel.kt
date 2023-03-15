package ru.zveron.lots_feed.filters_screen.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersSelectedCategoryHolder

class FiltersChildrenCategoriesViewModel(
    filtersSelectedCategoryHolder: FiltersSelectedCategoryHolder,
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
}