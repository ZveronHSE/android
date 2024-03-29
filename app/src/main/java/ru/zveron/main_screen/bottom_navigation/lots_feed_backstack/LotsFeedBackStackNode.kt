package ru.zveron.main_screen.bottom_navigation.lots_feed_backstack

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import kotlinx.parcelize.Parcelize
import ru.zveron.appyx.viewmodel.ViewModelParentNode
import ru.zveron.choose_item.ChooseItemNode
import ru.zveron.design.resources.ZveronText
import ru.zveron.lot_card.LotCardNavigator
import ru.zveron.lot_card.LotCardNode
import ru.zveron.lot_card.LotCardParams
import ru.zveron.lots_feed.feed.LotsFeedNavigator
import ru.zveron.lots_feed.feed.LotsFeedNode
import ru.zveron.lots_feed.filters_screen.FiltersNavigator
import ru.zveron.lots_feed.filters_screen.FiltersNode

class LotsFeedBackStackNode(
    buildContext: BuildContext,
    private val lotsFeedBackStackNavigator: LotsFeedBackStackNavigator,
    private val lotsFeedBackStackComponent: LotsFeedBackStackComponent = LotsFeedBackStackComponent(),
    lotsFeedBackPlugin: LotsFeedBackPlugin = lotsFeedBackStackComponent.getBackPlugin(),
    private val backstack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.RootCategory,
        savedStateMap = buildContext.savedStateMap,
        backPressHandler = lotsFeedBackPlugin,
    ),
): ViewModelParentNode<LotsFeedBackStackNode.NavTarget>(
    buildContext = buildContext,
    navModel = backstack,
    plugins = listOf(lotsFeedBackStackComponent),
), LotsFeedNavigator, FiltersNavigator, LotCardNavigator {
    sealed class NavTarget: Parcelable {
        @Parcelize
        object RootCategory: NavTarget()

        @Parcelize
        object Filters: NavTarget()

        @Parcelize
        data class PickItem(val title: ZveronText): NavTarget()

        @Parcelize
        data class LotCard(val params: LotCardParams): NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            NavTarget.RootCategory -> LotsFeedNode(
                buildContext,
                lotsFeedBackStackComponent.scope,
                this,
            )

            is NavTarget.Filters -> FiltersNode(
                buildContext,
                lotsFeedBackStackComponent.scope,
                this,
            )

            is NavTarget.PickItem -> ChooseItemNode(
                buildContext,
                navTarget.title,
                lotsFeedBackStackComponent.getChoseeItemItemProvider(),
            )

            is NavTarget.LotCard -> LotCardNode(
                buildContext,
                navTarget.params,
                this,
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

    override fun goToFilters() {
        backstack.push(NavTarget.Filters)
    }

    override fun goToSearch() {
        TODO("Not yet implemented")
    }

    override fun goToLot(lotId: Long) {
        lotsFeedBackStackNavigator.goToLot(lotId)
    }

    override fun chooseItem(title: ZveronText) {
        backstack.push(NavTarget.PickItem(title))
    }

    override fun startAuthorization() {
        lotsFeedBackStackNavigator.goToAuthorization()
    }

    override fun goToSeller(id: Long) {
        lotsFeedBackStackNavigator.goToLot(id)
    }
}