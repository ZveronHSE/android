package ru.zveron.appyx.operations

import com.bumble.appyx.core.navigation.NavElements
import com.bumble.appyx.core.navigation.NavModel
import com.bumble.appyx.core.navigation.Operation
import kotlinx.parcelize.Parcelize

@Parcelize
class CombineOperations<NavTarget, State>(
    private val operations: List<Operation<NavTarget, State>>,
) : Operation<NavTarget, State> {


    override fun isApplicable(elements: NavElements<NavTarget, State>): Boolean {
        // consequently execute operations to check if operation is applicable at each state
        operations.fold(elements) { accumulated, operation ->
            if (!operation.isApplicable(accumulated)) {
                return false
            }
            operation.invoke(accumulated)
        }
        return true
    }

    override fun invoke(elements: NavElements<NavTarget, State>): NavElements<NavTarget, State> {
        return operations.fold(elements) { accumulated, operation ->
            operation.invoke(accumulated)
        }
    }
}

fun <NavTarget, State> NavModel<NavTarget, State>.combineOperations(
    vararg operations: Operation<NavTarget, State>,
) {
    this.accept(CombineOperations(operations.toList()))
}