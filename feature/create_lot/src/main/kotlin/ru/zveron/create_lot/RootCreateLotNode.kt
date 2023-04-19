package ru.zveron.create_lot

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.backstack.BackStack
import kotlinx.parcelize.Parcelize
import ru.zveron.appyx.viewmodel.ViewModelParentNode
import ru.zveron.create_lot.categories_step.CategoriesStepNode
import ru.zveron.create_lot.general_step.GeneralStepNode

class RootCreateLotNode(
    buildContext: BuildContext,
    private val component: RootCreateLotComponent = RootCreateLotComponent(),
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.GeneralStep,
        savedStateMap = buildContext.savedStateMap,
    )
): ViewModelParentNode<RootCreateLotNode.NavTarget>(
    buildContext = buildContext,
    navModel = backStack,
    plugins = listOf(component),
) {
    sealed class NavTarget: Parcelable {
        @Parcelize
        object GeneralStep: NavTarget()

        @Parcelize
        object CategoriesStep: NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            NavTarget.GeneralStep -> GeneralStepNode(buildContext, component.scope)
            NavTarget.CategoriesStep -> CategoriesStepNode(buildContext, component.scope)
        }
    }

    @Composable
    override fun View(modifier: Modifier) {

        Children(
            navModel = backStack,
            modifier = modifier,
        )
    }
}