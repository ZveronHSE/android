package ru.zveron.main_screen.bottom_navigation.personal_profile_backstack

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.backstack.BackStack
import kotlinx.parcelize.Parcelize
import ru.zveron.appyx.viewmodel.ViewModelParentNode
import ru.zveron.personal_profile.profile_preview.PersonalProfileNode

class PersonalProfileBackstackNode(
    buildContext: BuildContext,
    private val navigator: PersonalProfileBackstackNavigator,
    private val component: PersonalProfileBackstackComponent = PersonalProfileBackstackComponent(),
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.PersonalProfile,
        savedStateMap = buildContext.savedStateMap,
    )
): ViewModelParentNode<PersonalProfileBackstackNode.NavTarget>(
    buildContext = buildContext,
    plugins = listOf(component),
    navModel = backStack,
) {
    sealed class NavTarget: Parcelable {
        @Parcelize
        object PersonalProfile: NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            NavTarget.PersonalProfile -> PersonalProfileNode(buildContext)
        }
    }

    @Composable
    override fun View(modifier: Modifier) {
        Children(navModel = backStack, modifier = modifier)
    }
}