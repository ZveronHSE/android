package ru.zveron.appyx.modal

import com.bumble.appyx.core.navigation.onscreen.OnScreenStateResolver

object ModalOnScreenResolver: OnScreenStateResolver<Modal.State> {
    override fun isOnScreen(state: Modal.State): Boolean {
        return when (state) {
            Modal.State.CREATED,
            Modal.State.DESTROYED -> false
            Modal.State.SHOWN -> true
        }
    }
}