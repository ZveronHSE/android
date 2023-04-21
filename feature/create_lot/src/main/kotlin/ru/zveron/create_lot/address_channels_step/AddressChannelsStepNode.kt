package ru.zveron.create_lot.address_channels_step

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.create_lot.address_channels_step.ui.AddressChannelsStep
import ru.zveron.create_lot.address_channels_step.ui.AddressChannelsStepViewModel

internal class AddressChannelsStepNode(
    buildContext: BuildContext,
    scope: Scope,
    private val navigator: AddressChannelsStepNavigator,
    private val component: AddressChannelsStepComponent = AddressChannelsStepComponent(),
): ViewModelNode(
    buildContext = buildContext,
    plugins = listOf(component),
) {
    init {
        component.scope.linkTo(scope)
    }

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<AddressChannelsStepViewModel>(
            scope = component.scope,
            viewModelStoreOwner = this,
            parameters = { parametersOf(navigator) },
        )

        val uiState = viewModel.uiState.collectAsState()

        AddressChannelsStep(
            address = viewModel.addressInputState.value,
            setAddress = viewModel::setAddress,
            addressChannelUiState = uiState.value,
            modifier = modifier,
            onBackClick = ::navigateUp,
            onAddressClick = viewModel::addressClicked,
            onCommunicationChannelsClick = viewModel::communicationChannelClicked,
            onContinueButtonClick = viewModel::onContinueClicked,
        )
    }
}