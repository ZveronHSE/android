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
import kotlinx.coroutines.flow.flowOf
import ru.zveron.appyx.bottom_navigation.BottomNavigationMode
import ru.zveron.appyx.bottom_navigation.BottomNavigationModeHolder

open class ViewModelNode(
    buildContext: BuildContext,
    view: NodeView = EmptyNodeView,
    plugins: List<Plugin> = emptyList(),
): Node(
    buildContext = buildContext,
    view = view,
    plugins = plugins,
), ViewModelStoreOwner, BottomNavigationModeHolder {
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

    override val viewModelStore: ViewModelStore
        get() = nodeViewModelStoreOwner

    @Composable
    fun ProvideViewModelStore(content: @Composable () -> Unit) {
        CompositionLocalProvider(
            LocalViewModelStoreOwner provides this,
        ) {
            content.invoke()
        }
    }

    override val bottomNavigationMode = flowOf(BottomNavigationMode.SHOW_BOTTOM_BAR)
}