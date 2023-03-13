package ru.zveron.lots_feed.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.scope.Scope
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.lots_feed.categories.ui.CategoriesViewModel
import ru.zveron.lots_feed.feed.ui.LotsFeed
import ru.zveron.lots_feed.feed.ui.LotsFeedViewModel

class LotsFeedNode(
    buildContext: BuildContext,
    scope: Scope,
    private val lotsFeedNavigator: LotsFeedNavigator,
    private val lotsFeedNodeArgument: LotsFeedNodeArgument,
    private val lotsFeedComponent: LotsFeedComponent = LotsFeedComponent(),
) : ViewModelNode(
    buildContext,
    plugins = listOf(lotsFeedComponent),
) {

    init {
        lotsFeedComponent.scope.linkTo(scope)
    }

    @Composable
    override fun View(modifier: Modifier) {
        val feedViewModel = koinViewModel<LotsFeedViewModel>(
            viewModelStoreOwner = this,
            scope = lotsFeedComponent.scope,
        )

        val categoriesViewModel = koinViewModel<CategoriesViewModel>(
            viewModelStoreOwner = this,
            scope = lotsFeedComponent.scope,
        )

        val filtersNavigation = remember {
            { lotsFeedNavigator.goToFilters(lotsFeedNodeArgument.categoryArgument?.id ?: 0) }
        }

        val feedUiState by feedViewModel.feedUiState.collectAsState()

        val categoriesUiState by categoriesViewModel.uiState.collectAsState()

        val categoryTitle by categoriesViewModel.categoryTitle.collectAsState()

        val canNavigateBack by categoriesViewModel.canNavigateBack.collectAsState()

        LotsFeed(
            categoryTitle = categoryTitle,
            feedUiState = feedUiState,
            categoriesUiState = categoriesUiState,
            modifier = modifier,
            hasBackButton = canNavigateBack,
            onNavigateBack = ::navigateUp,
            onCategoryClick = {
                categoriesViewModel.onCategoryClicked(it.id)
//                lotsFeedNavigator.goToCategory(CategoryArgument(it.id, it.title))
            },
            onSearchClicked = lotsFeedNavigator::goToSearch,
            onFiltersClicked = filtersNavigation,
        )
    }
}