package ru.zveron.favorites

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import ru.zveron.appyx.viewmodel.ViewModelNode

class FavoritesNode(
    buildContext: BuildContext,
    private val component: FavoritesComponent = FavoritesComponent(),
): ViewModelNode(
    buildContext,
    plugins = listOf(component),
) {
    @Composable
    override fun View(modifier: Modifier) {

    }
}