package ru.zveron.main_screen.bottom_navigation.user_lots_backstack

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.backstack.BackStack
import kotlinx.parcelize.Parcelize
import ru.zveron.appyx.viewmodel.ViewModelParentNode
import ru.zveron.user_lots.UserLotsNavigator
import ru.zveron.user_lots.UserLotsNode

class UserLotsBackStackNode(
    buildContext: BuildContext,
    private val userLotsBackstackNavigator: UserLotsBackstackNavigator,
    component: UserLotsBackStackComponent = UserLotsBackStackComponent(),
    private val backstack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.UserLots,
        savedStateMap = buildContext.savedStateMap,
    )
): ViewModelParentNode<UserLotsBackStackNode.NavTarget>(
    buildContext = buildContext,
    navModel = backstack,
    plugins = listOf(component),
), UserLotsNavigator {
    sealed class NavTarget: Parcelable {
        @Parcelize
        object UserLots: NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when(navTarget) {
            NavTarget.UserLots -> UserLotsNode(buildContext, this)
        }
    }

    @Composable
    override fun View(modifier: Modifier) {
        Children(
            navModel = backstack,
            modifier = modifier,
        )
    }

    override fun createLot() {
        userLotsBackstackNavigator.createUserLot()
    }

    override fun openLot(id: Long) {
        userLotsBackstackNavigator.goToLot(id)
    }
}