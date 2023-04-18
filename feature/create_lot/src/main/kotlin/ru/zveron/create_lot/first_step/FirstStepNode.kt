package ru.zveron.create_lot.first_step

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.scope.Scope
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.create_lot.first_step.ui.FirstStepLotCreate
import ru.zveron.create_lot.first_step.ui.FirstStepViewModel

internal class FirstStepNode(
    buildContext: BuildContext,
    scope: Scope,
    private val component: FirstStepComponent = FirstStepComponent(),
): ViewModelNode(
    buildContext = buildContext,
) {
    init {
        component.scope.linkTo(scope)
    }

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<FirstStepViewModel>(
            scope = component.scope,
            viewModelStoreOwner = this,
        )

        val rootCategoriesUiState = viewModel.rootCategoriesUiState.collectAsState()
        val photoUploadUiState = viewModel.photoUploadUiState.collectAsState()
        val continueButtonState = viewModel.continueButtonState.collectAsState()

        FirstStepLotCreate(
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