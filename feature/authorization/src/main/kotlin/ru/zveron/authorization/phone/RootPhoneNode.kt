package ru.zveron.authorization.phone

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.activeElement
import com.bumble.appyx.navmodel.backstack.operation.Push
import com.bumble.appyx.navmodel.backstack.operation.SingleTop
import com.bumble.appyx.navmodel.backstack.operation.push
import ru.zveron.appyx.combine.combineOperations
import ru.zveron.authorization.phone.password.PasswordInputNode
import ru.zveron.authorization.phone.phone_input.PhoneInputNode
import ru.zveron.authorization.phone.phone_input.deps.PhoneInputNavigator
import ru.zveron.authorization.phone.registration.RegistrationNode
import ru.zveron.authorization.phone.sms_code.SmsCodeNode
import ru.zveron.authorization.phone.sms_code.deps.SmsCodeDeps
import ru.zveron.authorization.phone.sms_code.deps.SmsCodeNavigator

class RootPhoneNode(
    buildContext: BuildContext,
    private val backStack: BackStack<RootPhoneNavTarget> = BackStack(
        initialElement = RootPhoneNavTarget.PhoneInput,
        savedStateMap = buildContext.savedStateMap,
    )
) : ParentNode<RootPhoneNavTarget>(
    buildContext = buildContext,
    navModel = backStack,
), PhoneInputNavigator, SmsCodeNavigator {
    override fun onChildFinished(child: Node) {
        super.onChildFinished(child)

        navigateUp()
    }

    override fun resolve(navTarget: RootPhoneNavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            RootPhoneNavTarget.PhoneInput -> PhoneInputNode(
                buildContext,
                this
            )
            is RootPhoneNavTarget.SmsCodeInput -> SmsCodeNode(
                buildContext,
                SmsCodeDeps(navTarget.phoneNumber, this),
            )
            RootPhoneNavTarget.PasswordInput -> PasswordInputNode(buildContext) {
                navigateToRegistration(it)
            }
            is RootPhoneNavTarget.Registration -> RegistrationNode(navTarget.phoneNumber, buildContext)
        }
    }

    @Composable
    override fun View(modifier: Modifier) {
        Children(
            navModel = backStack,
            modifier = modifier,
        )
    }

    override fun navigateToPassword() {
        if (backStack.activeElement == RootPhoneNavTarget.PhoneInput) {
            backStack.push(RootPhoneNavTarget.PasswordInput)
        } else {
            backStack.combineOperations(
                SingleTop.init(RootPhoneNavTarget.PhoneInput, backStack.elements.value),
                Push(RootPhoneNavTarget.PasswordInput),
            )
        }
    }

    override fun navigateToRegistration(phone: String) = backStack.push(RootPhoneNavTarget.Registration(phone))

//    suspend fun attachRootRegistration(): RegistrationNode {
//        return attachChild { backStack.newRoot(RootPhoneNavTarget.Registration) }
//    }

    override fun navigateToPasswordScreen() {
        navigateToPassword()
    }

    override fun navigateToSmsScreen(phone: String) {
        backStack.push(RootPhoneNavTarget.SmsCodeInput(phone))
    }
}