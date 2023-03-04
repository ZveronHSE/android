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
import ru.zveron.lots_feed.choose_item.ChooseItemNode
import ru.zveron.lots_feed.feed.CategoryArgument
import ru.zveron.lots_feed.feed.LotsFeedNavigator
import ru.zveron.lots_feed.feed.LotsFeedNode
import ru.zveron.lots_feed.feed.LotsFeedNodeArgument
import ru.zveron.lots_feed.filters_screen.FiltersNode
import ru.zveron.lots_feed.filters_screen.FiltersParams

class LotsFeedBackStackNode(
    buildContext: BuildContext,
    private val lotsFeedBackStackComponent: LotsFeedBackStackComponent = LotsFeedBackStackComponent(),
    private val backstack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.RootCategory,
        savedStateMap = buildContext.savedStateMap,
    ),
): ViewModelParentNode<LotsFeedBackStackNode.NavTarget>(
    buildContext = buildContext,
    navModel = backstack,
), LotsFeedNavigator {
    sealed class NavTarget: Parcelable {
        @Parcelize
        object RootCategory: NavTarget()

        @Parcelize
        data class ChildCategory(val category: CategoryArgument): NavTarget()

        @Parcelize
        data class Filters(val categoryId: Int): NavTarget()

        @Parcelize
        data class PickItem(
            val items: List<Pair<Int, String>>,
            val title: String,
            val onItemPicked: (Int) -> Unit,
        ): NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            NavTarget.RootCategory -> LotsFeedNode(
                buildContext,
                lotsFeedBackStackComponent.scope,
                this,
                LotsFeedNodeArgument(null),
            )

            is NavTarget.ChildCategory -> LotsFeedNode(
                buildContext,
                lotsFeedBackStackComponent.scope,
                this,
                LotsFeedNodeArgument(navTarget.category),
            )

            is NavTarget.Filters -> FiltersNode(
                buildContext,
                lotsFeedBackStackComponent.scope,
                // TODO: add lot form id
                FiltersParams(navTarget.categoryId, 0)
            )

            is NavTarget.PickItem -> ChooseItemNode(
                buildContext,
                navTarget.items,
                navTarget.title,
                navTarget.onItemPicked,
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

    override fun goToFilters(categoryId: Int) {
        backstack.push(NavTarget.Filters(categoryId))
    }

    override fun goToCategory(category: CategoryArgument) {
        backstack.push(NavTarget.ChildCategory(category))
    }

    override fun goToSearch() {
        TODO("Not yet implemented")
    }

    override fun goToLot(lotId: Long) {
        TODO("Not yet implemented")
    }
}