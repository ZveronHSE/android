package ru.zveron.user_profile.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class UserProfileViewModel(

): ViewModel() {
    private val _uiState = MutableStateFlow(UserProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {

    }

    fun onTabClick(userProfileTab: UserProfileTab) {

    }

    fun onLotLikeClick(lotId: Long) {

    }

    fun onLotClick(lotId: Long) {

    }

    fun onReviewsClick() {

    }
}