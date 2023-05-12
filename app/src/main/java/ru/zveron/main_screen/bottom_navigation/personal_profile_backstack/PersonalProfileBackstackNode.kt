package ru.zveron.main_screen.bottom_navigation.personal_profile_backstack

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import ru.zveron.appyx.viewmodel.ViewModelParentNode
import ru.zveron.personal_profile.ProfileUiInfo
import ru.zveron.personal_profile.edit_profile.EditProfileNode
import ru.zveron.personal_profile.profile_preview.PersonalProfileNavigator
import ru.zveron.personal_profile.profile_preview.PersonalProfileNode

class PersonalProfileBackstackNode(
    buildContext: BuildContext,
    private val navigator: PersonalProfileBackstackNavigator,
    component: PersonalProfileBackstackComponent = PersonalProfileBackstackComponent(),
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.PersonalProfile,
        savedStateMap = buildContext.savedStateMap,
    )
): ViewModelParentNode<PersonalProfileBackstackNode.NavTarget>(
    buildContext = buildContext,
    plugins = listOf(component),
    navModel = backStack,
), PersonalProfileNavigator {
    sealed class NavTarget: Parcelable {
        @Parcelize
        object PersonalProfile: NavTarget()

        @Parcelize
        data class EditProfile(val profile: ProfileUiInfo): NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            NavTarget.PersonalProfile -> PersonalProfileNode(buildContext, this)
            is NavTarget.EditProfile -> EditProfileNode(buildContext, navTarget.profile)
        }
    }

    @Composable
    override fun View(modifier: Modifier) {
        Children(navModel = backStack, modifier = modifier)
    }

    override fun reattachMainScreen() {
        navigator.reattachMainScreen()
    }

    override fun editProfile(profileUiInfo: ProfileUiInfo) {
        backStack.push(NavTarget.EditProfile(profileUiInfo))
    }

    override fun onChildFinished(child: Node) {
        if (child !is EditProfileNode) {
            return
        }

        val profileUiInfo = child.getEditResult()
        lifecycleScope.launch {
            val profileNode = waitForChildAttached<PersonalProfileNode>()
            profileNode.updateProfileInfo(profileUiInfo)
        }

    }
}