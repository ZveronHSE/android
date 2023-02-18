package ru.zveron.lots_feed.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import ru.zveron.appyx.viewmodel.ViewModelNode
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

        LaunchedEffect(feedViewModel) {
            feedViewModel.loadLots()
        }

        Box(modifier.fillMaxSize()) {
            Text("test")
        }
    }
}