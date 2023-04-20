package ru.zveron.create_lot.details_step

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.create_lot.details_step.ui.DetailsStep
import ru.zveron.create_lot.details_step.ui.DetailsStepViewModel

internal class DetailsStepNode(
    buildContext: BuildContext,
    scope: Scope,
    private val navigator: DetailsStepNavigator,
    private val component: DetailsStepComponent = DetailsStepComponent(),
): ViewModelNode(
    buildContext,
    plugins = listOf(component),
) {
    init {
        component.scope.linkTo(scope)
    }

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<DetailsStepViewModel>(
            scope = component.scope,
            viewModelStoreOwner = this,
            parameters = { parametersOf(navigator) },
        )

        val parametersUiState = viewModel.parametersUiState.collectAsState()
        val continueButtonState = viewModel.continueButtonState.collectAsState()

        DetailsStep(
            parametersUiState = parametersUiState.value,
            continueButtonState = continueButtonState.value,
            description = viewModel.descriptionInputState.value,
            setDescription = viewModel::setDescriptionInput,
            modifier = modifier,
            onBackClicked = ::navigateUp,
            onParameterClick = viewModel::onParameterClick,
            onContinueButtonClick = viewModel::onContinueClick,
        )
    }
}