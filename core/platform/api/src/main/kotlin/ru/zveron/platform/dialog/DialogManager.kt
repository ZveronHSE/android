package ru.zveron.platform.dialog

import ru.zveron.design.resources.ZveronText

interface DialogManager {
    suspend fun requestDialog(request: DialogParams): DialogResult
}

sealed class DialogResult {
    object Confirm: DialogResult()

    object Dismiss: DialogResult()
}

data class DialogParams(
    val title: ZveronText? = null,
    val message: ZveronText? = null,
    val confirmButtonLabel: ZveronText,
    val dismissButtonLabel: ZveronText? = null,
)