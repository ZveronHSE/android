package ru.zveron.user_lots

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.user_lots.ui.UserLots
import ru.zveron.user_lots.ui.UserLotsViewModel

class UserLotsNode(
    buildContext: BuildContext,
    private val navigator: UserLotsNavigator,
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
            parameters = { parametersOf(navigator) },
        )

        val uiState = viewmodel.uiState.collectAsState()

        UserLots(
            state = uiState.value,
            modifier = modifier,
            onAddLotClick = viewmodel::onAddLotClick,
            onLotClick = viewmodel::onLotClick,
            onTabClick = viewmodel::onTabClick,
            onRetryClicked = viewmodel::onRetryClick,
        )
    }
}