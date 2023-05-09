package ru.zveron.root

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.zveron.platform.dialog.DialogManager
import ru.zveron.platform.dialog.DialogParams
import ru.zveron.platform.dialog.DialogResult

class RootDialogManagerImpl: DialogManager, RootDialogStateHolder {
    private val _requests = MutableStateFlow<DialogRequest?>(null)
    override val requests = _requests.asStateFlow()

    @OptIn(InternalCoroutinesApi::class)
    override suspend fun requestDialog(request: DialogParams): DialogResult = coroutineScope {
        val newDeferrable = CompletableDeferred<DialogResult>()
        _requests.update { DialogRequestImpl(request, newDeferrable) }

        newDeferrable.invokeOnCompletion(onCancelling = true) {
            _requests.update { null }
        }

        newDeferrable.await()
    }

    private inner class DialogRequestImpl(
        override val params: DialogParams,
        private val deferred: CompletableDeferred<DialogResult>
    ): DialogRequest {

        override fun submitResult(response: DialogResult) {
            deferred.complete(response)
            _requests.update { null }
        }
    }
}
