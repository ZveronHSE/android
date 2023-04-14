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
import com.bumble.appyx.core.children.nodeOrNull
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.navigation.NavModel
import com.bumble.appyx.core.node.EmptyParentNodeView
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.core.node.ParentNodeView
import com.bumble.appyx.core.plugin.Plugin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import ru.zveron.appyx.bottom_navigation.BottomNavigationMode
import ru.zveron.appyx.bottom_navigation.BottomNavigationModeHolder

abstract class ViewModelParentNode<NavTarget : Any>(
    navModel: NavModel<NavTarget, *>,
    buildContext: BuildContext,
    view: ParentNodeView<NavTarget> = EmptyParentNodeView(),
    childKeepMode: ChildEntry.KeepMode = Appyx.defaultChildKeepMode,
    childAware: ChildAware<ParentNode<NavTarget>> = ChildAwareImpl(),
    plugins: List<Plugin> = listOf(),
) : ParentNode<NavTarget>(
    navModel,
    buildContext,
    view,
    childKeepMode,
    childAware,
    plugins,
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

    override val bottomNavigationMode: Flow<BottomNavigationMode> = navModel.screenState.flatMapLatest { screenState ->
        val bottomNavigationModeHolder = screenState.onScreen
            .map { childOrCreate(it.key).nodeOrNull }
            .filterIsInstance<BottomNavigationModeHolder>()
            .takeIf { it.isNotEmpty() }
            ?.lastOrNull()

        bottomNavigationModeHolder?.bottomNavigationMode ?: flowOf(BottomNavigationMode.SHOW_BOTTOM_BAR)
    }

}