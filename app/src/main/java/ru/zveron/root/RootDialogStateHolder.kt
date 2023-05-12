package ru.zveron.root

import kotlinx.coroutines.flow.StateFlow
import ru.zveron.platform.dialog.DialogParams
import ru.zveron.platform.dialog.DialogResult

interface RootDialogStateHolder {
    val requests: StateFlow<DialogRequest?>
}

interface DialogRequest {
    val params: DialogParams

    fun submitResult(response: DialogResult)
}