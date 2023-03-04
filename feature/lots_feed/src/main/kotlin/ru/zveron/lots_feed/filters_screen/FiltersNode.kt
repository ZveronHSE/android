package ru.zveron.lots_feed.filters_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.lots_feed.filters_screen.ui.FilterScreen
import ru.zveron.lots_feed.filters_screen.ui.FiltersViewModel

class FiltersNode(
    buildContext: BuildContext,
    parentScope: Scope,
    private val filtersParams: FiltersParams,
    private val filtersComponent: FiltersComponent = FiltersComponent(),
): ViewModelNode(
    buildContext,
    plugins = listOf(filtersComponent),
) {
    init {
        filtersComponent.scope.linkTo(parentScope)
    }

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<FiltersViewModel>(
            viewModelStoreOwner = this,
            scope = filtersComponent.scope,
            parameters = { parametersOf(filtersParams) }
        )

        val parametersState by viewModel.parametersUiState.collectAsState()

        FilterScreen(
            parametersState = parametersState,
            modifier = modifier,
            onBackClicked = ::navigateUp,
        )
    }

}