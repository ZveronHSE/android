package ru.zveron.lots_feed.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.lots_feed.categories.ui.CategoriesViewModel
import ru.zveron.lots_feed.categories.ui.CategoryUiState
import ru.zveron.lots_feed.feed.ui.LotsFeed
import ru.zveron.lots_feed.feed.ui.LotsFeedUiState
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

        val feedUiState = feedViewModel.feedUiState.collectAsState()

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

        val query = remember { feedViewModel.queryState }

        val focusManager = LocalFocusManager.current
        LaunchedEffect(feedViewModel) {
            feedViewModel.clearFocusFlow.collect {
                focusManager.clearFocus()
            }
        }

        val isRefreshing = when (val uiState = feedUiState.value) {
            is LotsFeedUiState.Success -> uiState.isRefreshing
            LotsFeedUiState.Loading -> false
        }

        LotsFeed(
            categoryTitle = categoryTitle,
            feedUiState = feedUiState.value,
            categoriesUiState = categoriesUiState,
            parametersUiState = parametersUiState,
            currentSortType = currentSortType,
            query = query.value,
            setQuery = feedViewModel::setQuery,
            modifier = modifier,
            hasBackButton = canNavigateBack,
            onNavigateBack = ::navigateUp,
            onCategoryClick = categoryClicker,
            onFiltersClicked = feedViewModel::goToFilters,
            onSortTypeSelected = feedViewModel::sortTypeSelected,
            onParameterClick = parametersViewModel::parameterClicked,
            onLotLikeClick = feedViewModel::lotLikedClicked,
            onLotClick = feedViewModel::lotClicked,
            isRefreshing = isRefreshing,
            onRefresh = feedViewModel::refreshLots,
            refreshEnabled = feedUiState.value is LotsFeedUiState.Success,
        )
    }
}