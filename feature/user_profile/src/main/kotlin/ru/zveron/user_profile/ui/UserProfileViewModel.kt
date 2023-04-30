package ru.zveron.user_profile.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.wrappers.ListWrapper
import ru.zveron.favorites.data.FavoritesRepository
import ru.zveron.models.lots.Lot
import ru.zveron.user_profile.R
import ru.zveron.user_profile.UserProfileParams
import ru.zveron.user_profile.data.UserProfileRepository
import ru.zveron.design.R as DesignR

internal class UserProfileViewModel(
    private val params: UserProfileParams,
    private val userProfileRepository: UserProfileRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UserProfileUiState>(UserProfileUiState.Loading)
    private val _activeLots = MutableStateFlow<ListWrapper<LotUiState>>(ListWrapper(emptyList()))
    private val _closedLots = MutableStateFlow<ListWrapper<LotUiState>>(ListWrapper(emptyList()))

    val uiState = _uiState.asStateFlow()

    init {
        launchLoadProfile()
    }

    private fun launchLoadProfile() {
        viewModelScope.launch {
            try {
                _uiState.update { UserProfileUiState.Loading }
                val profile = userProfileRepository.getUserProfilePage(params.id)
                val activeLots = ListWrapper(profile.activeLots.map { mapLot(it) })
                val closedLots = ListWrapper(profile.closedLots.map { mapLot(it) })

                val avatar = if (profile.avatarUrl.isNotBlank()) {
                    ZveronImage.RemoteImage(profile.avatarUrl)
                } else {
                    ZveronImage.ResourceImage(DesignR.drawable.ic_no_avatar)
                }

                _uiState.update {
                    UserProfileUiState.Success(
                        photo = avatar,
                        displayedName = ZveronText.ArgResource(
                            R.string.name_display_format,
                            profile.name,
                            profile.surname,
                        ),
                        address = profile.address,
                        rating = profile.rating,
                        reviewAmount = profile.reviewAmount,
                        currentLots = activeLots,
                        currentTab = UserProfileTab.ACTIVE,
                    )
                }

                _activeLots.update { activeLots }
                _closedLots.update { closedLots }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { UserProfileUiState.Error }
                Log.e("User profile", "Error loading profile", e)
            }
        }
    }

    private fun mapLot(lot: Lot): LotUiState {
        return LotUiState(
            id = lot.id,
            title = lot.title,
            price = lot.price,
            date = lot.publicationDate,
            image = ZveronImage.RemoteImage(lot.photoUrl),
            isLiked = mutableStateOf(lot.isFavorite),
        )
    }

    fun onTabClick(userProfileTab: UserProfileTab) {
        _uiState.update { state ->
            when (state) {
                is UserProfileUiState.Success -> {
                    if (state.currentTab == userProfileTab) {
                        state
                    } else {
                        val lots =
                            if (userProfileTab == UserProfileTab.ACTIVE) _activeLots.value else _closedLots.value
                        state.copy(currentTab = userProfileTab, currentLots = lots)
                    }
                }

                else -> state
            }
        }
    }

    fun onLotLikeClick(lotId: Long) {
        val lotUiState = when (val state = _uiState.value) {
            is UserProfileUiState.Success -> state.currentLots.list.find { it.id == lotId }
            else -> null
        } ?: return

        val newLikeStatus = !lotUiState.isLiked.value
        lotUiState.isLiked.value = newLikeStatus

        viewModelScope.launch {
            try {
                if (newLikeStatus) {
                    favoritesRepository.addLotToFavorites(lotId)
                } else {
                    favoritesRepository.removeLotFromFavorites(lotId)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("User profile", "Error liking lot", e)
            }
        }
    }

    fun onLotClick(lotId: Long) {

    }

    fun onReviewsClick() {

    }

    fun retry() {
        launchLoadProfile()
    }
}