package ru.zveron.appyx.modal.operation

import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import ru.zveron.appyx.modal.Modal
import ru.zveron.appyx.modal.ModalElements

@Parcelize
class Dismiss<T> : ModalOperation<T> {
    override fun isApplicable(elements: ModalElements<T>): Boolean {
        return true
    }

    override fun invoke(elements: ModalElements<T>): ModalElements<T> {
        return elements.transitionTo(Modal.State.DESTROYED)
    }
}

fun <T: Any> Modal<T>.dismiss() {
    accept(Dismiss())
}