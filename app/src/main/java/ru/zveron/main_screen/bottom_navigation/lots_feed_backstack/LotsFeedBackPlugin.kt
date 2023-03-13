package ru.zveron.main_screen.bottom_navigation.lots_feed_backstack

import com.bumble.appyx.core.navigation.NavElement
import com.bumble.appyx.core.navigation.NavModelAdapter
import com.bumble.appyx.core.navigation.backpresshandlerstrategies.BaseBackPressHandlerStrategy
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.Pop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.zveron.lots_feed.categories.domain.SelectedCategoriesInteractor

class LotsFeedBackPlugin(
    private val selectedCategoriesInteractor: SelectedCategoriesInteractor,
) : BaseBackPressHandlerStrategy<LotsFeedBackStackNode.NavTarget, BackStack.State>() {
    override fun onBackPressed() {
        val shouldSwitchCategory = navModel.screenState.value.let(::containsLotsFeedScreen)
        if (shouldSwitchCategory) {
            selectedCategoriesInteractor.resetCurrentCategory()
        } else {
            navModel.accept(Pop())
        }
    }

    override val canHandleBackPressFlow: Flow<Boolean> by lazy {
        combine(
            navModel.screenState,
            selectedCategoriesInteractor.currentCategorySelection
        ) { screenState, categorySelection ->
            (containsLotsFeedScreen(screenState) && !categorySelection.isEmpty())
                    || areThereStashedElements(screenState.onScreen + screenState.offScreen)
        }
    }

    private fun containsLotsFeedScreen(
        screenState: NavModelAdapter.ScreenState<LotsFeedBackStackNode.NavTarget, BackStack.State>
    ): Boolean {
        return screenState.onScreen.any { isLotsFeedScreen(it) }
    }

    private fun isLotsFeedScreen(navElement: NavElement<LotsFeedBackStackNode.NavTarget, out BackStack.State>): Boolean {
        return navElement.key.navTarget is LotsFeedBackStackNode.NavTarget.ChildCategory || navElement.key.navTarget is LotsFeedBackStackNode.NavTarget.RootCategory
    }

    private fun areThereStashedElements(
        elements: List<NavElement<LotsFeedBackStackNode.NavTarget, out BackStack.State>>,
    ): Boolean {
        return elements.any { it.targetState == BackStack.State.STASHED }
    }
}