package ru.zveron.user_lots

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.spotlight.Spotlight
import kotlinx.parcelize.Parcelize
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zveron.appyx.spotlight.activate
import ru.zveron.appyx.viewmodel.ViewModelParentNode
import ru.zveron.user_lots.active.ActiveLotsNode
import ru.zveron.user_lots.archive.ArchiveLotsNode
import ru.zveron.user_lots.ui.TabNavigator
import ru.zveron.user_lots.ui.UserLots
import ru.zveron.user_lots.ui.UserLotsViewModel

class UserLotsNode(
    buildContext: BuildContext,
    private val navigator: UserLotsNavigator,
    private val component: UserLotsComponent = UserLotsComponent(),
    private val spotlight: Spotlight<NavTarget> = Spotlight(
        items = listOf(NavTarget.Active, NavTarget.Archive),
        initialActiveIndex = 0,
        savedStateMap = buildContext.savedStateMap,
    ),
): ViewModelParentNode<UserLotsNode.NavTarget>(
    buildContext = buildContext,
    navModel = spotlight,
    plugins = listOf(component),
), TabNavigator {
    @Parcelize
    sealed class NavTarget: Parcelable {
        object Active: NavTarget()

        object Archive: NavTarget()
    }

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<UserLotsViewModel>(
            scope = component.scope,
            viewModelStoreOwner = this,
            parameters = { parametersOf(navigator, this) },
        )

        val uiState = viewModel.uiState.collectAsState()
        val tab = uiState.value.currentTab

        UserLots(
            currentTab = tab,
            modifier = modifier,
            onTabClick = viewModel::onTabClick,
        ) {
            Children(
                navModel = spotlight,
                modifier = it,
            )
        }
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when(navTarget) {
            NavTarget.Active -> ActiveLotsNode(
                buildContext = buildContext,
                scope = component.scope,
                userLotsNavigator = navigator,
            )
            NavTarget.Archive -> ArchiveLotsNode(
                buildContext = buildContext,
                scope = component.scope,
                userLotsNavigator = navigator,
            )
        }
    }

    override fun openActiveTab() {
        spotlight.activate(NavTarget.Active)
    }

    override fun openArchiveTab() {
        spotlight.activate(NavTarget.Archive)
    }
}