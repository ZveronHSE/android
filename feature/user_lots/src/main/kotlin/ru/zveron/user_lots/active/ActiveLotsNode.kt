package ru.zveron.user_lots.active

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.user_lots.UserLotsNavigator
import ru.zveron.user_lots.ui.LotsList
import ru.zveron.user_lots.ui.LotsUiState

internal class ActiveLotsNode(
    buildContext: BuildContext,
    scope: Scope,
    private val userLotsNavigator: UserLotsNavigator,
    private val component: ActiveLotsComponent = ActiveLotsComponent(),
): ViewModelNode(
    buildContext = buildContext,
    plugins = listOf(component),
) {

    init {
        component.scope.linkTo(scope)
    }

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<ActiveLotsViewModel>(
            scope = component.scope,
            viewModelStoreOwner = this,
            parameters = { parametersOf(userLotsNavigator) },
        )

        val uiState = viewModel.uiState.collectAsState()

        val isRefreshing = when (val state = uiState.value) {
            is LotsUiState.Success -> state.isRefreshing
            else -> false
        }

        LotsList(
            state = uiState.value,
            isRefreshing = isRefreshing,
            showAddLotButton = true,
            modifier = modifier,
            refreshEnabled = uiState.value is LotsUiState.Success,
            onAddLotClick = viewModel::onAddLotClick,
            onLotClick = viewModel::onLotClick,
            onRefresh = viewModel::refreshLots,
            onRetryClicked = viewModel::onRetryClick,
        )
    }
}