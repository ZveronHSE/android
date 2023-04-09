package ru.zveron.lot_card

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.lot_card.ui.LotCard
import ru.zveron.lot_card.ui.LotCardViewModel

class LotCardNode(
    buildContext: BuildContext,
    private val params: LotCardParams,
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
            parameters = { parametersOf(params) }
        )

        val uiState by viewmodel.uiState.collectAsState()

        LotCard(
            state = uiState,
            modifier = modifier,
            onBackClicked = ::navigateUp,
        )
    }
}