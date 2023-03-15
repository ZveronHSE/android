package ru.zveron.lots_feed.filters_screen.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zveron.lots_feed.choose_item.ChooseItem
import ru.zveron.lots_feed.choose_item.ChooseItemItemProvider
import ru.zveron.lots_feed.choose_item.ChooseItemUiState
import ru.zveron.lots_feed.filters_screen.data.categories.ChildCategory
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersChildrenCategoryHolder

class ChildCategoryItemProvider(
    filtersChildrenCategoryHolder: FiltersChildrenCategoryHolder,
    private val filtersSetSelectedCategoryInteractor: FiltersSetSelectedCategoryInteractor,
): ChooseItemItemProvider {
    private val scope = CoroutineScope(Dispatchers.IO)

    override val uiState: StateFlow<ChooseItemUiState> = filtersChildrenCategoryHolder.childrenCategoryState.map {
        when (it) {
            ChildCategory.Loading -> ChooseItemUiState.Loading
            is ChildCategory.Success -> {
                val items = it.categories.map { category -> ChooseItem(category.id, category.name) }
                ChooseItemUiState.Success(items)
            }
        }
    }.stateIn(scope, SharingStarted.Lazily, ChooseItemUiState.Loading)

    override fun itemPicked(id: Int) {
        scope.launch(Dispatchers.IO) {
            filtersSetSelectedCategoryInteractor.setInnerCategory(id)
        }
    }
}