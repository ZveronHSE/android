package ru.zveron.personal_profile.profile_preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.personal_profile.profile_preview.ui.PersonalProfile
import ru.zveron.personal_profile.profile_preview.ui.PersonalProfileViewModel

class PersonalProfileNode(
    buildContext: BuildContext,
    private val component: PersonalProfileComponent = PersonalProfileComponent(),
): ViewModelNode(
    buildContext = buildContext,
    plugins = listOf(component),
) {
    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<PersonalProfileViewModel>(
            scope = component.scope,
            viewModelStoreOwner = this,
        )

        val uiState = viewModel.uiState.collectAsState()

        PersonalProfile(
            uiState = uiState.value,
            modifier = modifier,
            onEditProfileClick = viewModel::onEditPorfileClick,
            onLogoutClick = viewModel::onLogoutTapped,
            onDeleteProfileClick = viewModel::onDeleteAccountTapped,
            onRetryClick = viewModel::onRetryClick,
            onRefresh = viewModel::refresh,
        )
    }
}