package ru.zveron.user_lots

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.user_lots.ui.UserLotsViewModel

class UserLotsNode(
    buildContext: BuildContext,
    private val component: UserLotsComponent = UserLotsComponent(),
): ViewModelNode(
    buildContext,
    plugins = listOf(component),
) {
    @Composable
    override fun View(modifier: Modifier) {
        val viewmodel = koinViewModel<UserLotsViewModel>(
            scope = component.scope,
            viewModelStoreOwner = this,
        )
    }
}