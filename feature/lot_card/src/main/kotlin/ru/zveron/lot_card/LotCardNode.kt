package ru.zveron.lot_card

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zveron.appyx.bottom_navigation.BottomNavigationMode
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.lot_card.ui.LotCard
import ru.zveron.lot_card.ui.LotCardViewModel

class LotCardNode(
    buildContext: BuildContext,
    private val params: LotCardParams,
    private val navigator: LotCardNavigator,
    private val lotCardComponent: LotCardComponent = LotCardComponent(),
): ViewModelNode(
    buildContext = buildContext,
    plugins = listOf(lotCardComponent),
) {
    @Composable
    override fun View(modifier: Modifier) {
        val viewmodel = koinViewModel<LotCardViewModel>(
            scope = lotCardComponent.scope,
            viewModelStoreOwner = this,
            parameters = { parametersOf(params, navigator) }
        )

        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            viewmodel.onPermissionResult(it)
        }

        LaunchedEffect(viewmodel) {
            viewmodel.permissionEffectFlow.collect {
                permissionLauncher.launch(it)
            }
        }

        val uiState by viewmodel.uiState.collectAsState()

        LotCard(
            state = uiState,
            modifier = modifier,
            onBackClicked = ::navigateUp,
            onActionClick = viewmodel::onActionClicked,
            onSellerClick = viewmodel::onSellerClicked,
            onRetryClicked = viewmodel::retry,
        )
    }

    override val bottomNavigationMode = flowOf(BottomNavigationMode.HIDE_BOTTOM_BAR)
}