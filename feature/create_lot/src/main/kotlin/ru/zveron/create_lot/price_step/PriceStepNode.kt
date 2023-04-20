package ru.zveron.create_lot.price_step

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.create_lot.price_step.ui.PriceStep
import ru.zveron.create_lot.price_step.ui.PriceStepViewModel

internal class PriceStepNode(
    buildContext: BuildContext,
    scope: Scope,
    private val priceStepNavigator: PriceStepNavigator,
    private val component: PriceStepComponent = PriceStepComponent(),
): ViewModelNode(
    buildContext = buildContext,
    plugins = listOf(component),
) {
    init {
        component.scope.linkTo(scope)
    }

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<PriceStepViewModel>(
            scope = component.scope,
            viewModelStoreOwner = this,
            parameters = { parametersOf(priceStepNavigator) },
        )

        val canContinue = viewModel.canContinueState.collectAsState()

        PriceStep(
            price = viewModel.priceState.value,
            setPrice = viewModel::setPrice,
            isNegotiated = viewModel.negotiatedState.value,
            onNegotiatedClicked = viewModel::toggleNegotiated,
            canContinue = canContinue.value,
            modifier = modifier,
            onContinueClick = viewModel::continueClicked,
        )
    }
}