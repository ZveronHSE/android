package ru.zveron.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.favorites.ui.Favorites
import ru.zveron.favorites.ui.FavoritesViewModel
import ru.zveron.favorites.ui.state.FavoritesLotsUiState

class FavoritesNode(
    buildContext: BuildContext,
    private val favoritesNodeNavigator: FavoritesNodeNavigator,
    private val component: FavoritesComponent = FavoritesComponent(),
): ViewModelNode(
    buildContext,
    plugins = listOf(component),
) {
    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<FavoritesViewModel>(
            viewModelStoreOwner = this,
            scope = component.scope,
        )

        val categoriesState by viewModel.categoriesUiState.collectAsState()
        val contentState by viewModel.contentUiState.collectAsState()

        val isRefreshing = when (val uiState = contentState) {
            is FavoritesLotsUiState.Success -> uiState.isRefreshing
            else -> false
        }

        Favorites(
            categoryState = categoriesState,
            contentState = contentState,
            modifier = modifier,
            onCategoryTabClicked = viewModel::categorySelected,
            onRetryClicked = viewModel::retryClicked,
            onLotClick = favoritesNodeNavigator::openLot,
            onLotLikeClick = viewModel::onLotLikeClick,
            onDeleteAllClicked = viewModel::onRemoveAllClicked,
            onDeleteUnactiveClicked = viewModel::onRemoveUnactiveClicked,
            isRefreshing = isRefreshing,
            onRefresh = viewModel::refresh,
            refreshEnabled = contentState is FavoritesLotsUiState.Success,
        )
    }
}