package ru.zveron.appyx.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.get

class NodeContainerViewModel: ViewModel() {
    private val viewModelStores = mutableMapOf<String, ViewModelStore>()

    fun clear(nodeId: String) {
        val viewModelStore = viewModelStores.remove(nodeId)
        viewModelStore?.clear()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelStores.values.forEach { it.clear() }
        viewModelStores.clear()
    }

    fun getViewModelStoreForNode(nodeId: String): ViewModelStore =
        viewModelStores.getOrPut(nodeId) { ViewModelStore() }
}

internal fun ViewModelStoreOwner.getNodeViewModelInstance(): NodeContainerViewModel {
    return ViewModelProvider(this).get()
}