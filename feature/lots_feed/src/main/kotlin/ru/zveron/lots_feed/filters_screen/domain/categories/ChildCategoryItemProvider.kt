package ru.zveron.lots_feed.filters_screen.domain.categories

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.zveron.choose_item.ChooseItem
import ru.zveron.choose_item.ChooseItemItemProvider
import ru.zveron.choose_item.ChooseItemUiState
import ru.zveron.design.resources.ZveronText
import ru.zveron.lots_feed.filters_screen.data.categories.ChildCategory
import ru.zveron.lots_feed.filters_screen.data.categories.FiltersChildrenCategoryRepository

internal class ChildCategoryItemProvider(
    filtersChildrenCategoryRepository: FiltersChildrenCategoryRepository,
    private val filtersSetSelectedCategoryInteractor: FiltersSetSelectedCategoryInteractor,
): ChooseItemItemProvider {
    private val scope = CoroutineScope(Dispatchers.IO)

    override val uiState: StateFlow<ChooseItemUiState> = filtersChildrenCategoryRepository.childrenCategoryState.map {
        when (it) {
            ChildCategory.Loading -> ChooseItemUiState.Loading
            is ChildCategory.Success -> {
                val items = it.categories.map { category -> ChooseItem(category.id, ZveronText.RawString(category.name)) }
                ChooseItemUiState.Success(items)
            }
        }
    }.stateIn(scope, SharingStarted.Lazily, ChooseItemUiState.Loading)

    override fun itemPicked(id: Int) {
        filtersSetSelectedCategoryInteractor.setInnerCategory(id)
    }
}