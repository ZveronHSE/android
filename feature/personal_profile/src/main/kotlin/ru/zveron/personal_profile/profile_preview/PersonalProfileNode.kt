package ru.zveron.personal_profile.profile_preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.personal_profile.ProfileUiInfo
import ru.zveron.personal_profile.mappings.toDomain
import ru.zveron.personal_profile.profile_preview.data.ProfileInfo
import ru.zveron.personal_profile.profile_preview.ui.PersonalProfile
import ru.zveron.personal_profile.profile_preview.ui.PersonalProfileViewModel

class PersonalProfileNode(
    buildContext: BuildContext,
    private val navigator: PersonalProfileNavigator,
    private val component: PersonalProfileComponent = PersonalProfileComponent(),
): ViewModelNode(
    buildContext = buildContext,
    plugins = listOf(component),
) {
    private val personalProfileRepository by lazy {
        component.getPersonalProfileRepository()
    }

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<PersonalProfileViewModel>(
            scope = component.scope,
            viewModelStoreOwner = this,
            parameters = { parametersOf(navigator) },
        )

        val uiState = viewModel.uiState.collectAsState()

        PersonalProfile(
            uiState = uiState.value,
            modifier = modifier,
            onEditProfileClick = viewModel::onEditProfileClick,
            onLogoutClick = viewModel::onLogoutTapped,
            onDeleteProfileClick = viewModel::onDeleteAccountTapped,
            onRetryClick = viewModel::onRetryClick,
            onRefresh = viewModel::refresh,
        )
    }

    fun updateProfileInfo(profileUiInfo: ProfileUiInfo) {
        val currentProfileInfo = personalProfileRepository.getCachedProfileInfo()

        val profileInfo = ProfileInfo(
            id = currentProfileInfo.id,
            name = profileUiInfo.name,
            surname = profileUiInfo.surname,
            avatarUrl = profileUiInfo.avatarUrl.orEmpty(),
            rating = currentProfileInfo.rating,
            addressInfo = profileUiInfo.addressUiInfo.toDomain(),
        )

        personalProfileRepository.updateWithProfileInfo(profileInfo)
    }
}