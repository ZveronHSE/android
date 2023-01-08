package ru.zveron.authorization.phone

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import ru.zveron.authorization.phone.phone_input.PhoneInputNode

class RootPhoneNode(
    buildContext: BuildContext,
    private val backStack: BackStack<RootPhoneNavTarget> = BackStack(
        initialElement = RootPhoneNavTarget.PhoneInput,
        savedStateMap = buildContext.savedStateMap,
    )
): ParentNode<RootPhoneNavTarget>(
    buildContext = buildContext,
    navModel = backStack,
) {
    override fun resolve(navTarget: RootPhoneNavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            RootPhoneNavTarget.PhoneInput -> PhoneInputNode(buildContext)
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