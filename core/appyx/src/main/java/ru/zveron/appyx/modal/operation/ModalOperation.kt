package ru.zveron.appyx.modal.operation

import com.bumble.appyx.core.navigation.Operation
import ru.zveron.appyx.modal.Modal

interface ModalOperation<T>: Operation<T, Modal.State>