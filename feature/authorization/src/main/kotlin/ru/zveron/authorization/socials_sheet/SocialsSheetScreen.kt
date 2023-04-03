package ru.zveron.authorization.socials_sheet

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zveron.appyx.modal.BottomSheetStateHolder
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.authorization.R
import ru.zveron.design.components.ActionButton
import ru.zveron.design.components.BottomSheet
import ru.zveron.design.components.LoginButton
import ru.zveron.design.theme.GoogleColors
import ru.zveron.design.theme.VkColors
import ru.zveron.design.theme.ZveronTheme

class SocialsSheetScreen(
    buildContext: BuildContext,
    private val socialsComponent: SocialsComponent = SocialsComponent(),
    private val navigateToPhoneAuthorization: () -> Unit,
): ViewModelNode(buildContext = buildContext), BottomSheetStateHolder, SocialsModalBlocker {
    private val shouldBlockModalBottomSheet = MutableStateFlow(false)

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<SocialsViewModel>(
            scope = socialsComponent.scope,
            viewModelStoreOwner = this,
            parameters = { parametersOf(this as SocialsModalBlocker) }
        )

        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.onResult(result.data!!)
            }
        }

        LaunchedEffect(viewModel) {
            viewModel.startAuthFlow.collect {
                launcher.launch(it)
            }
        }

        LaunchedEffect(viewModel) {
            viewModel.finishScreenFlow.collect { navigateUp() }
        }

        val isLoading by viewModel.isLoading

        SocialsScreen(
            isLoading = isLoading,
            modifier = modifier,
            onPhoneButtonClick = ::onPhoneButtonClick,
            onGoogleButtonClick = viewModel::googleSignInIntentClicked,
        )
    }

    private fun onPhoneButtonClick() {
        navigateUp()
        navigateToPhoneAuthorization.invoke()
    }

    override val shouldBlockBottomSheet: Flow<Boolean> = shouldBlockModalBottomSheet.asStateFlow()

    override fun blockModalBottomSheet() {
        shouldBlockModalBottomSheet.update { true }
    }

    override fun unblockModalBottomSheet() {
        shouldBlockModalBottomSheet.update { false }
    }
}

@Composable
private fun SocialsScreen(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onGoogleButtonClick: () -> Unit = {},
    onVkButtonClick: () -> Unit = {},
    onPhoneButtonClick: () -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterHorizontally),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                LoginButton(loginButtonColors = GoogleColors, onClick = onGoogleButtonClick)

                LoginButton(loginButtonColors = VkColors, onClick = onVkButtonClick)
            }

            Spacer(Modifier.height(24.dp))

            ActionButton(
                onClick = onPhoneButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(stringResource(R.string.login_with_number))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun PreviewSocials() {
    BottomSheet {
        SocialsScreen(false)
    }
}

@Preview
@Composable
fun PreviewSocialsLoading() {
    ZveronTheme {
        BottomSheet {
            SocialsScreen(true)
        }
    }
}