package ru.zveron.main_screen.bottom_navigation.favorites_backstack

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.backstack.BackStack
import kotlinx.parcelize.Parcelize
import ru.zveron.appyx.viewmodel.ViewModelParentNode
import ru.zveron.favorites.FavoritesNode

class FavoritesBackstackNode(
    buildContext: BuildContext,
    favoritesBackstackComponent: FavoritesBackstackComponent = FavoritesBackstackComponent(),
    private val backstack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.Favorites,
        savedStateMap = buildContext.savedStateMap,
    )
): ViewModelParentNode<FavoritesBackstackNode.NavTarget>(
    buildContext = buildContext,
    navModel = backstack,
    plugins = listOf(favoritesBackstackComponent),
) {
    sealed class NavTarget: Parcelable {
        @Parcelize
        object Favorites: NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            NavTarget.Favorites -> FavoritesNode(
                buildContext,
            )
        }
    }

    @Composable
    override fun View(modifier: Modifier) {
        Children(
            navModel = backstack,
            modifier = modifier,
        )
    }
}