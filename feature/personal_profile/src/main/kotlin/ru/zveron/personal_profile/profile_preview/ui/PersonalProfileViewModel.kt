package ru.zveron.personal_profile.profile_preview.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.authorization.storage.AuthorizationStorage
import ru.zveron.design.R as DesignR
import ru.zveron.design.resources.ZveronImage
import ru.zveron.personal_profile.profile_preview.PersonalProfileNavigator
import ru.zveron.personal_profile.profile_preview.data.LogoutRepository
import ru.zveron.personal_profile.profile_preview.data.PersonalProfileRepository

internal class PersonalProfileViewModel(
    private val personalProfileRepository: PersonalProfileRepository,
    private val logoutRepository: LogoutRepository,
    private val navigator: PersonalProfileNavigator,
    private val authorizationStorage: AuthorizationStorage,
): ViewModel() {
    private val _uiState = MutableStateFlow<PersonalProfileUiState>(PersonalProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadPersonalProfile()
    }

    private fun loadPersonalProfile() {
        viewModelScope.launch {
            try {
                _uiState.update { PersonalProfileUiState.Loading }

                val profileInfo = personalProfileRepository.getPersonalProfileInfo()

                _uiState.update {
                    PersonalProfileUiState.Success(
                        avatar = if (profileInfo.avatarUrl.isNotBlank()) {
                            ZveronImage.RemoteImage(profileInfo.avatarUrl)
                        } else {
                            ZveronImage.ResourceImage(DesignR.drawable.ic_no_avatar)
                        },
                        rating = profileInfo.rating,
                        displayName = "${profileInfo.name} ${profileInfo.surname}",
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.d("Personal profile", "Error loading profile info", e)
                _uiState.update { PersonalProfileUiState.Error }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                _uiState.update {
                    when (it) {
                        is PersonalProfileUiState.Success -> it.copy(isRefreshing = true)
                        else -> it
                    }
                }

                val profileInfo = personalProfileRepository.getPersonalProfileInfo()

                _uiState.update {
                    PersonalProfileUiState.Success(
                        avatar = if (profileInfo.avatarUrl.isNotBlank()) {
                            ZveronImage.RemoteImage(profileInfo.avatarUrl)
                        } else {
                            ZveronImage.ResourceImage(DesignR.drawable.ic_no_avatar)
                        },
                        rating = profileInfo.rating,
                        displayName = "${profileInfo.name} ${profileInfo.surname}",
                        isRefreshing = false,
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.d("Personal profile", "Error loading profile info", e)
                _uiState.update { PersonalProfileUiState.Error }
            }
        }
    }

    fun onLogoutTapped() {
        viewModelScope.launch {
            try {
                _uiState.update {
                    when (it) {
                        is PersonalProfileUiState.Success -> it.copy(isLogoutting = true)
                        else -> it
                    }
                }
                logoutRepository.logout()
                authorizationStorage.clearAuthorization()
                navigator.reattachMainScreen()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.d("Personal profile", "Error logging out", e)
            }
        }
    }

    fun onDeleteAccountTapped() {

    }

    fun onEditPorfileClick() {

    }

    fun onRetryClick() {
        loadPersonalProfile()
    }
}