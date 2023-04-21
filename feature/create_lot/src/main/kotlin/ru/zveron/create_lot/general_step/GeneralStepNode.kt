package ru.zveron.create_lot.general_step

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.create_lot.general_step.ui.GeneralStepLotCreate
import ru.zveron.create_lot.general_step.ui.GeneralStepViewModel

internal class GeneralStepNode(
    buildContext: BuildContext,
    scope: Scope,
    private val generalStepNavigator: GeneralStepNavigator,
    private val component: GeneralStepComponent = GeneralStepComponent(),
) : ViewModelNode(
    buildContext = buildContext,
    plugins = listOf(component),
) {
    init {
        component.scope.linkTo(scope)
    }

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<GeneralStepViewModel>(
            scope = component.scope,
            viewModelStoreOwner = this,
            parameters = { parametersOf(generalStepNavigator) }
        )

        val contract =
            rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                it?.let { viewModel.loadPhoto(it) }
            }

        LaunchedEffect(viewModel) {
            viewModel.requestPhotoFlow.collect {
                contract.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        val rootCategoriesUiState = viewModel.rootCategoriesUiState.collectAsState()
        val photoUploadUiState = viewModel.photoUploadUiState.collectAsState()
        val continueButtonState = viewModel.continueButtonState.collectAsState()

        GeneralStepLotCreate(
            rootCategoriesUiState = rootCategoriesUiState.value,
            photoUploadUiState = photoUploadUiState.value,
            nameInput = viewModel.nameInputState.value,
            setName = viewModel::onNameInputted,
            continueButtonState = continueButtonState.value,
            modifier = modifier.windowInsetsPadding(WindowInsets.ime),
            onCloseClick = ::navigateUp,
            onRootCategoryClick = viewModel::onRootCategoryClick,
            onAddPhotoClick = viewModel::onAddPhotoClick,
            onPhotoRetryClick = viewModel::onPhotoRetryClick,
            onContinueClick = viewModel::onContinueClick,
        )
    }
}