package ru.zveron.personal_profile.profile_preview.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.design.R
import ru.zveron.design.resources.ZveronImage

internal class PersonalProfileViewModel(

): ViewModel() {
    private val _uiState = MutableStateFlow<PersonalProfileUiState>(PersonalProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadPersonalProfile()
    }

    private fun loadPersonalProfile() {
        // TODO: add actual implementation
        viewModelScope.launch {
            _uiState.update { PersonalProfileUiState.Loading }

            delay(1000L)

            _uiState.update {
                PersonalProfileUiState.Success(
                    avatar = ZveronImage.ResourceImage(R.drawable.ic_no_avatar),
                    displayName = "Егор Шпак",
                    rating = 4.2,
                )
            }
        }
    }
}