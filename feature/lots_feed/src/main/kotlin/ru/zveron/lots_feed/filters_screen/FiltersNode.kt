package ru.zveron.lots_feed.filters_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.zveron.appyx.bottom_navigation.BottomNavigationMode
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.lots_feed.filters_screen.ui.FilterScreen
import ru.zveron.lots_feed.filters_screen.ui.parameters.FiltersViewModel
import ru.zveron.lots_feed.filters_screen.ui.categories.FiltersChildrenCategoriesViewModel
import ru.zveron.lots_feed.filters_screen.ui.categories.FiltersRootCategoriesViewModel
import ru.zveron.lots_feed.filters_screen.ui.lot_form.LotFormViewModel

class FiltersNode(
    buildContext: BuildContext,
    parentScope: Scope,
    private val filtersNavigator: FiltersNavigator,
    private val filtersComponent: FiltersComponent = FiltersComponent(),
) : ViewModelNode(
    buildContext,
    plugins = listOf(filtersComponent),
) {
    init {
        filtersComponent.scope.linkTo(parentScope)
    }

    @Composable
    override fun View(modifier: Modifier) {
        val filtersViewModel = koinViewModel<FiltersViewModel>(
            viewModelStoreOwner = this,
            scope = filtersComponent.scope,
            parameters = { parametersOf(filtersNavigator) },
        )

        val rootCategoriesViewModel = koinViewModel<FiltersRootCategoriesViewModel>(
            viewModelStoreOwner = this,
            scope = filtersComponent.scope,
            parameters = { parametersOf(filtersNavigator) },
        )

        val childrenCategoriesViewModel = koinViewModel<FiltersChildrenCategoriesViewModel>(
            viewModelStoreOwner = this,
            scope = filtersComponent.scope,
            parameters = { parametersOf(filtersNavigator) },
        )

        val lotFormViewModel = koinViewModel<LotFormViewModel>(
            viewModelStoreOwner = this,
            scope = filtersComponent.scope,
            parameters = { parametersOf(filtersNavigator) },
        )

        val parametersState by filtersViewModel.parametersUiState.collectAsState()
        val rootCategoriesState by rootCategoriesViewModel.rootCategoriesState.collectAsState()
        val childCategoriesState by childrenCategoriesViewModel.uiState.collectAsState()
        val lotFormState by lotFormViewModel.uiState.collectAsState()

        FilterScreen(
            rootCategoriesUiState = rootCategoriesState,
            childCategoriesUiState = childCategoriesState,
            parametersState = parametersState,
            lotFormUiState = lotFormState,
            modifier = modifier,
            onBackClicked = ::navigateUp,
            onRootCategorySelected = rootCategoriesViewModel::rootCategorySelected,
            onLotFormClicked = lotFormViewModel::lotFormRowClicked,
            onChildCategoryClicked = childrenCategoriesViewModel::childCategoryClicked,
        )
    }

    override val bottomNavigationMode: Flow<BottomNavigationMode> =
        flowOf(BottomNavigationMode.HIDE_BOTTOM_BAR)

}