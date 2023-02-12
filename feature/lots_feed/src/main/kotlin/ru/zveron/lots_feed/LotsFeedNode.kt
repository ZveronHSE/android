package ru.zveron.lots_feed

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import ru.zveron.appyx.viewmodel.ViewModelNode

class LotsFeedNode(
    buildContext: BuildContext,
    private val lotsFeedComponent: LotsFeedComponent = LotsFeedComponent(),
): ViewModelNode(
    buildContext,
    plugins = listOf(lotsFeedComponent),
) {
    @Composable
    override fun View(modifier: Modifier) {

    }
}