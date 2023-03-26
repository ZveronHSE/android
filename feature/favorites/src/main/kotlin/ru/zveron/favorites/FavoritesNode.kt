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

class FavoritesNode(
    buildContext: BuildContext,
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

        Favorites(
            categoryState = categoriesState,
            contentState = contentState,
            modifier = modifier,
            onCategoryTabClicked = viewModel::categorySelected,
            onRetryClicked = viewModel::retryClicked,
            // TODO: replace with real navigation
            onLotClick = {},
            onLotLikeClick = viewModel::onLotLikeClick,
        )
    }
}