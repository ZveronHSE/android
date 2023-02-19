package ru.zveron.lots_feed.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.lots_feed.categories.ui.CategoriesViewModel
import ru.zveron.lots_feed.feed.ui.LotsFeed
import ru.zveron.lots_feed.feed.ui.LotsFeedViewModel

class LotsFeedNode(
    buildContext: BuildContext,
    private val lotsFeedComponent: LotsFeedComponent = LotsFeedComponent(),
): ViewModelNode(
    buildContext,
    plugins = listOf(lotsFeedComponent),
) {
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

        LaunchedEffect(feedViewModel) {
            feedViewModel.loadLots()
        }

        LaunchedEffect(categoriesViewModel) {
            categoriesViewModel.loadCategories(null)
        }

        val feedUiState by feedViewModel.feedUiState.collectAsState()

        val categoriesUiState by categoriesViewModel.uiState.collectAsState()

        LotsFeed(
            feedUiState = feedUiState,
            categoriesUiState = categoriesUiState,
            modifier = modifier,
            onNavigateBack = ::navigateUp,
        )
    }
}