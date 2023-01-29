package ru.zveron.appyx.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.EmptyNodeView
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.NodeView
import com.bumble.appyx.core.plugin.Plugin

open class ViewModelNode(
    buildContext: BuildContext,
    view: NodeView = EmptyNodeView,
    plugins: List<Plugin> = emptyList(),
): Node(
    buildContext = buildContext,
    view = view,
    plugins = plugins,
), ViewModelStoreOwner {
    private val containerViewModel: NodeContainerViewModel
        get() = (integrationPoint as ViewModelActivityIntegrationPoint).viewModel

    private val nodeViewModelStoreOwner by lazy {
        containerViewModel.getViewModelStoreForNode(id)
    }

    init {
        lifecycle.addObserver(object: DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                if (!(integrationPoint as ViewModelActivityIntegrationPoint).isChangingConfiguration()) {
                    containerViewModel.clear(id)
                }
            }
        })
    }

    override fun getViewModelStore(): ViewModelStore {
        return nodeViewModelStoreOwner
    }

    @Composable
    fun ProvideViewModelStore(content: @Composable () -> Unit) {
        CompositionLocalProvider(
            LocalViewModelStoreOwner provides this,
        ) {
            content.invoke()
        }
    }
}