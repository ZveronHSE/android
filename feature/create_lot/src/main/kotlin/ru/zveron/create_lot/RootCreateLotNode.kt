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
import ru.zveron.create_lot.first_step.FirstStepNode

class RootCreateLotNode(
    buildContext: BuildContext,
    private val component: RootCreateLotComponent = RootCreateLotComponent(),
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.FirstStep,
        savedStateMap = buildContext.savedStateMap,
    )
): ViewModelParentNode<RootCreateLotNode.NavTarget>(
    buildContext = buildContext,
    navModel = backStack,
    plugins = listOf(component),
) {
    sealed class NavTarget: Parcelable {
        @Parcelize
        object FirstStep: NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            NavTarget.FirstStep -> FirstStepNode(buildContext, component.scope)
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