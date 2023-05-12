package ru.zveron.appyx.operations

import com.bumble.appyx.core.navigation.NavKey
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.BackStackElement
import com.bumble.appyx.navmodel.backstack.BackStackElements
import com.bumble.appyx.navmodel.backstack.active
import com.bumble.appyx.navmodel.backstack.operation.BackStackOperation
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ClearWithNewRoot<NavTarget>(
    private val element: @RawValue NavTarget,
) : BackStackOperation<NavTarget> {
    override fun isApplicable(elements: BackStackElements<NavTarget>): Boolean = true

    override fun invoke(elements: BackStackElements<NavTarget>): BackStackElements<NavTarget> {
        val current = elements.active
        requireNotNull(current) { "No previous elements, state=$elements" }

        return listOf(
            current.transitionTo(
                newTargetState = BackStack.State.DESTROYED,
                operation = this,
            ),
            BackStackElement(
                key = NavKey(element),
                fromState = BackStack.State.CREATED,
                targetState = BackStack.State.ACTIVE,
                operation = this,
            )
        )
    }
}

fun <T: Any> BackStack<T>.clearWithNewRoot(element: T) {
    accept(ClearWithNewRoot(element))
}