package ru.zveron.appyx.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.bumble.appyx.Appyx
import com.bumble.appyx.core.children.ChildAware
import com.bumble.appyx.core.children.ChildAwareImpl
import com.bumble.appyx.core.children.ChildEntry
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.navigation.NavModel
import com.bumble.appyx.core.node.EmptyParentNodeView
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.ParentNodeView
import com.bumble.appyx.core.plugin.Plugin

abstract class ViewModelParentNode<NavTarget: Any>(
    navModel: NavModel<NavTarget, *>,
    buildContext: BuildContext,
    view: ParentNodeView<NavTarget> = EmptyParentNodeView(),
    childKeepMode: ChildEntry.KeepMode = Appyx.defaultChildKeepMode,
    childAware: ChildAware<ParentNode<NavTarget>> = ChildAwareImpl(),
    plugins: List<Plugin> = listOf(),
): ParentNode<NavTarget>(
    navModel,
    buildContext,
    view,
    childKeepMode,
    childAware,
    plugins,
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