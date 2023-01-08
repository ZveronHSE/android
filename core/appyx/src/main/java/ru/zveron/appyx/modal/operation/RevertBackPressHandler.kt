package ru.zveron.appyx.modal.operation

import com.bumble.appyx.core.navigation.backpresshandlerstrategies.BaseBackPressHandlerStrategy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.zveron.appyx.modal.Modal

class RevertBackPressHandler<NavTarget : Any> :
    BaseBackPressHandlerStrategy<NavTarget, Modal.State>() {
    override fun onBackPressed() {
        navModel.accept(Dismiss())
    }

    override val canHandleBackPressFlow: Flow<Boolean> by lazy {
        navModel.elements.map { navElements ->
            navElements.any { it.targetState == Modal.State.SHOWN }
        }
    }
}