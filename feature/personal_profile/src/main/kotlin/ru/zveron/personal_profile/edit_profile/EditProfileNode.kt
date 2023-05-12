package ru.zveron.personal_profile.edit_profile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.bumble.appyx.core.modality.BuildContext
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zveron.appyx.bottom_navigation.BottomNavigationMode
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.personal_profile.ProfileUiInfo
import ru.zveron.personal_profile.edit_profile.ui.EditProfile
import ru.zveron.personal_profile.edit_profile.ui.EditProfileViewModel
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import java.io.File

class EditProfileNode(
    buildContext: BuildContext,
    private val params: ProfileUiInfo,
    private val component: EditProfileComponent = EditProfileComponent(),
) : ViewModelNode(
    buildContext = buildContext,
    plugins = listOf(component),
) {
    private var cameraUri: Uri? = null
    private var editResult: ProfileUiInfo? = null

    @Composable
    override fun View(modifier: Modifier) {
        val context = LocalContext.current

        val viewModel = koinViewModel<EditProfileViewModel>(
            scope = component.scope,
            viewModelStoreOwner = this,
            parameters = { parametersOf(params) },
        )

        LaunchedEffect(viewModel) {
            viewModel.finishEditResultFlow.collect {
                editResult = it
                finish()
                navigateUp()
            }
        }

        val takePictureContract = rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                cameraUri?.let {
                    viewModel.uploadAvatar(it)
                }
            }
        }

        val pickVisualMediaContract = rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {
                viewModel.uploadAvatar(it)
            }
        }

        val launchCameraPicture = remember {
            {
                lifecycleScope.launch {
                    lifecycle.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                        val uri = getFileUri(context)
                        cameraUri = uri
                        takePictureContract.launch(cameraUri)
                    }
                }
            }
        }

        val launchGalleryPicture = remember {
            {
                pickVisualMediaContract.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        }

        val editProfileUiState = viewModel.editPorfileUiState.collectAsState()

        EditProfile(
            name = viewModel.nameInputState.value,
            setName = viewModel::setName,
            surname = viewModel.surnameInputState.value,
            setSurname = viewModel::setSurname,
            editProfileUiState = editProfileUiState.value,
            modifier = modifier,
            onBackClick = ::navigateUp,
            onGalleryPhotoClick = { launchGalleryPicture.invoke() },
            onCameraPhotoClick = { launchCameraPicture.invoke() },
            onReadyClick = viewModel::submitClicked,
            onPhotoRetryClick = viewModel::photoRetryClicked,
        )
    }

    override val bottomNavigationMode = flowOf(BottomNavigationMode.HIDE_BOTTOM_BAR)

    private fun getFileUri(context: Context): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", context.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            context.applicationContext,
            "zveron.provider",
            tmpFile
        )
    }
    fun getEditResult(): ProfileUiInfo {
        return requireNotNull(editResult)
    }
}