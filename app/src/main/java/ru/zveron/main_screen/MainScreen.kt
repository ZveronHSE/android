package ru.zveron.main_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.spotlight.Spotlight
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zveron.appyx.bottom_navigation.BottomNavigationMode
import ru.zveron.appyx.spotlight.activate
import ru.zveron.appyx.viewmodel.ViewModelParentNode
import ru.zveron.design.components.BottomNavigation
import ru.zveron.design.components.BottomNavigationItem
import ru.zveron.design.resources.ZveronText
import ru.zveron.main_screen.bottom_navigation.BottomNavigationNavTarget
import ru.zveron.main_screen.bottom_navigation.BottomTabsNavigator
import ru.zveron.main_screen.bottom_navigation.favorites_backstack.FavoritesBackstackNavigator
import ru.zveron.main_screen.bottom_navigation.favorites_backstack.FavoritesBackstackNode
import ru.zveron.main_screen.bottom_navigation.lots_feed_backstack.LotsFeedBackStackNavigator
import ru.zveron.main_screen.bottom_navigation.lots_feed_backstack.LotsFeedBackStackNode
import ru.zveron.main_screen.bottom_navigation.user_lots_backstack.UserLotsBackStackNode
import ru.zveron.main_screen.bottom_navigation.user_lots_backstack.UserLotsBackstackNavigator

internal class MainScreen(
    buildContext: BuildContext,
    private val mainScreenNavigator: MainScreenNavigator,
    private val mainScreenComponent: MainScreenComponent = MainScreenComponent(),
    private val spotlight: Spotlight<BottomNavigationNavTarget> = Spotlight(
        items = listOf(
            BottomNavigationNavTarget.LotsFeed,
            BottomNavigationNavTarget.Favorites,
            BottomNavigationNavTarget.UserLots,
        ),
        savedStateMap = buildContext.savedStateMap,
    ),
) : ViewModelParentNode<BottomNavigationNavTarget>(
    buildContext = buildContext,
    navModel = spotlight,
    plugins = listOf(mainScreenComponent),
), BottomTabsNavigator, LotsFeedBackStackNavigator, FavoritesBackstackNavigator, UserLotsBackstackNavigator {

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<MainScreenViewModel>(
            scope = mainScreenComponent.scope,
            viewModelStoreOwner = this,
            parameters = { parametersOf(mainScreenNavigator, this as BottomTabsNavigator) },
        )

        val items by viewModel.state.collectAsState()

        Screen(items, modifier = modifier)
    }

    override fun resolve(navTarget: BottomNavigationNavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            BottomNavigationNavTarget.LotsFeed -> LotsFeedBackStackNode(buildContext, this)

            BottomNavigationNavTarget.Favorites -> FavoritesBackstackNode(buildContext, this)

            BottomNavigationNavTarget.UserLots -> UserLotsBackStackNode(buildContext, this)
        }
    }

    @Composable
    private fun Screen(
        items: List<BottomNavigationItem>,
        modifier: Modifier = Modifier,
    ) {
        val isBottomBarVisible =
            bottomNavigationMode.collectAsState(initial = BottomNavigationMode.SHOW_BOTTOM_BAR)

        Surface(
            modifier = modifier,
            color = MaterialTheme.colors.background
        ) {
            Box(Modifier.fillMaxSize()) {
                Children(
                    navModel = spotlight,
                    modifier = Modifier.fillMaxSize(),
                )

                if (isBottomBarVisible.value == BottomNavigationMode.SHOW_BOTTOM_BAR) {
                    BottomNavigation(
                        items = items,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(start = 16.dp, end = 16.dp, bottom = 34.dp),
                    )
                }
            }

        }
    }

    override fun openLotsFeedBackstack() {
        spotlight.activate(BottomNavigationNavTarget.LotsFeed)
    }

    override fun openFavoritesBackstack() {
        spotlight.activate(BottomNavigationNavTarget.Favorites)
    }

    override fun openUserLotsBackstack() {
        spotlight.activate(BottomNavigationNavTarget.UserLots)
    }

    override fun goToAuthorization() {
        mainScreenNavigator.openAuthorization()
    }

    override fun goToLot(id: Long) {
        mainScreenNavigator.openLot(id)
    }

    override fun pickItem(title: ZveronText) {
        mainScreenNavigator.pickItem(title)
    }

    override fun createUserLot() {
        mainScreenNavigator.createLot()
    }

}