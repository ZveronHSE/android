package ru.zveron.personal_profile.profile_preview.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.authorization.storage.AuthorizationStorage
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.personal_profile.ProfileUiInfo
import ru.zveron.personal_profile.R
import ru.zveron.personal_profile.mappings.toUiModel
import ru.zveron.personal_profile.profile_preview.PersonalProfileNavigator
import ru.zveron.personal_profile.profile_preview.data.DeleteAccountRepository
import ru.zveron.personal_profile.profile_preview.data.LogoutRepository
import ru.zveron.personal_profile.profile_preview.data.PersonalProfileRepository
import ru.zveron.personal_profile.profile_preview.data.ProfileInfoState
import ru.zveron.platform.dialog.DialogManager
import ru.zveron.platform.dialog.DialogParams
import ru.zveron.platform.dialog.DialogResult
import ru.zveron.design.R as DesignR

internal class PersonalProfileViewModel(
    private val personalProfileRepository: PersonalProfileRepository,
    private val logoutRepository: LogoutRepository,
    private val navigator: PersonalProfileNavigator,
    private val authorizationStorage: AuthorizationStorage,
    private val deleteAccountRepository: DeleteAccountRepository,
    private val dialogManager: DialogManager,
): ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    private val _isLogoutting = MutableStateFlow(false)
    private val _isDeleting = MutableStateFlow(false)

    val uiState = combine(
        personalProfileRepository.profileInfoState,
        _isRefreshing,
        _isLogoutting,
        _isDeleting,
    ) { profileState, refreshing, isLogout, isDelete ->
        when (profileState) {
            ProfileInfoState.Error -> PersonalProfileUiState.Error
            ProfileInfoState.Loading -> PersonalProfileUiState.Loading
            is ProfileInfoState.Success -> {
                val profileInfo = profileState.profileInfo

                PersonalProfileUiState.Success(
                    avatar = if (profileInfo.avatarUrl.isNotBlank()) {
                        ZveronImage.RemoteImage(profileInfo.avatarUrl)
                    } else {
                        ZveronImage.ResourceImage(DesignR.drawable.ic_no_avatar)
                    },
                    rating = profileInfo.rating,
                    displayName = "${profileInfo.name} ${profileInfo.surname}",
                    isRefreshing = refreshing,
                    isLogoutting = isLogout,
                    isDeleting = isDelete,
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, PersonalProfileUiState.Loading)

    init {
        loadPersonalProfile()
    }

    private fun loadPersonalProfile() {
        viewModelScope.launch { personalProfileRepository.fetchPersonalProfileInfo() }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.update { true }
            personalProfileRepository.fetchPersonalProfileInfo()
            _isRefreshing.update { false }
        }
    }

    fun onLogoutTapped() {
        viewModelScope.launch {
            try {
                _isLogoutting.update { true }
                logoutRepository.logout()
                authorizationStorage.clearAuthorization()
                navigator.reattachMainScreen()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.d("Personal profile", "Error logging out", e)
                _isLogoutting.update { false }
            }
        }
    }

    fun onDeleteAccountTapped() {
        viewModelScope.launch {
            val dialogParams = DialogParams(
                title = ZveronText.RawResource(R.string.delete_account_dialog_title),
                message = ZveronText.RawResource(R.string.delete_account_dialog_message),
                confirmButtonLabel = ZveronText.RawResource(R.string.delete_account_dialog_confirm_button),
                dismissButtonLabel = ZveronText.RawResource(R.string.delete_account_dialog_dismiss_button),
            )

            val dialogResult = dialogManager.requestDialog(dialogParams)
            if (dialogResult != DialogResult.Confirm) {
                return@launch
            }

            try {
                _isDeleting.update { true }
                deleteAccountRepository.deleteAccount()
                authorizationStorage.clearAuthorization()
                navigator.reattachMainScreen()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.d("Personal profile", "Error deleting profile", e)
                _isDeleting.update { true }
            }
        }
    }

    fun onEditProfileClick() {
        val profileInfo = personalProfileRepository.getCachedProfileInfo()
        val profileUiInfo = ProfileUiInfo(
            avatarUrl = profileInfo.avatarUrl,
            name = profileInfo.name,
            surname = profileInfo.surname,
            addressUiInfo = profileInfo.addressInfo.toUiModel(),
        )

        navigator.editProfile(profileUiInfo)
    }

    fun onRetryClick() {
        loadPersonalProfile()
    }
}