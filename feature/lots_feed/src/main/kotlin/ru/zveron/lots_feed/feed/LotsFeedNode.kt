package ru.zveron.lots_feed.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.lots_feed.categories.ui.CategoriesViewModel
import ru.zveron.lots_feed.categories.ui.CategoryUiState
import ru.zveron.lots_feed.feed.ui.LotsFeed
import ru.zveron.lots_feed.feed.ui.LotsFeedViewModel
import ru.zveron.lots_feed.feed.ui.parameters.ParametersViewModel

class LotsFeedNode(
    buildContext: BuildContext,
    scope: Scope,
    private val lotsFeedNavigator: LotsFeedNavigator,
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
            parameters = { parametersOf(lotsFeedNavigator) },
        )

        val categoriesViewModel = koinViewModel<CategoriesViewModel>(
            viewModelStoreOwner = this,
            scope = lotsFeedComponent.scope,
            parameters = { parametersOf(lotsFeedNavigator) },
        )

        val parametersViewModel = koinViewModel<ParametersViewModel>(
            viewModelStoreOwner = this,
            scope = lotsFeedComponent.scope,
            parameters = { parametersOf(lotsFeedNavigator) },
        )

        val feedUiState by feedViewModel.feedUiState.collectAsState()

        val categoriesUiState by categoriesViewModel.uiState.collectAsState()

        val categoryTitle by categoriesViewModel.categoryTitle.collectAsState()

        val canNavigateBack by categoriesViewModel.canNavigateBack.collectAsState()

        val currentSortType by feedViewModel.currentSortType.collectAsState()

        val parametersUiState by parametersViewModel.uiState.collectAsState()

        val categoryClicker = remember {
            { categoriesUiState: CategoryUiState ->
                categoriesViewModel.onCategoryClicked(
                    categoriesUiState.id
                )
            }
        }

        LotsFeed(
            categoryTitle = categoryTitle,
            feedUiState = feedUiState,
            categoriesUiState = categoriesUiState,
            parametersUiState = parametersUiState,
            currentSortType = currentSortType,
            modifier = modifier,
            hasBackButton = canNavigateBack,
            onNavigateBack = ::navigateUp,
            onCategoryClick = categoryClicker,
            onSearchClicked = lotsFeedNavigator::goToSearch,
            onFiltersClicked = feedViewModel::goToFilters,
            onSortTypeSelected = feedViewModel::sortTypeSelected,
        )
    }
}