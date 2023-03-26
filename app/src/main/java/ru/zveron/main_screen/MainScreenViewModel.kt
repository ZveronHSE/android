package ru.zveron.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zveron.authorization.domain.AuthorizationEventsEmitter
import ru.zveron.authorization.storage.AuthorizationStorage
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText
import ru.zveron.main_screen.bottom_navigation.BottomNavigationItem
import ru.zveron.main_screen.bottom_navigation.BottomTabsNavigator
import ru.zveron.design.components.BottomNavigationItem as DesignBottomNavigationItem

class MainScreenViewModel(
    private val mainScreenNavigator: MainScreenNavigator,
    private val authorizationStorage: AuthorizationStorage,
    private val bottomTabsNavigator: BottomTabsNavigator,
    private val authorizationEventsEmitter: AuthorizationEventsEmitter,
) : ViewModel() {
    private val currentSelectedTab = MutableStateFlow(BottomNavigationItem.LOTS_FEED)
    private val handlers = mapOf(
        BottomNavigationItem.LOTS_FEED to this::lotsFeedTapped,
        BottomNavigationItem.FAVORITES to this::favouritesTapped,
        BottomNavigationItem.CREATE_LOT to this::createLotTapped,
        BottomNavigationItem.CHATS to this::messagesTapped,
        BottomNavigationItem.PROFILE to this::profileTapped,
    )

    val state = currentSelectedTab.map {
        mapToState(it)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, mapToState(BottomNavigationItem.LOTS_FEED))


    private fun mapToState(selectedItem: BottomNavigationItem): List<DesignBottomNavigationItem> {
        val tabs = mutableListOf<DesignBottomNavigationItem>()

        tabs.add(mapTab(BottomNavigationItem.LOTS_FEED, selectedItem))
        tabs.add(mapTab(BottomNavigationItem.FAVORITES, selectedItem))
        tabs.add(mapTab(BottomNavigationItem.CREATE_LOT, selectedItem))
        tabs.add(mapTab(BottomNavigationItem.CHATS, selectedItem))
        tabs.add(mapTab(BottomNavigationItem.PROFILE, selectedItem))

        return tabs
    }

    private fun mapTab(
        item: BottomNavigationItem,
        selectedItem: BottomNavigationItem
    ): DesignBottomNavigationItem {
        val isSelected = item == selectedItem
        val iconId = if (isSelected) item.selectedRes else item.unselectedRes
        return DesignBottomNavigationItem(
            title = ZveronText.RawResource(item.labelRes),
            image = ZveronImage.ResourceImage(iconId),
            isSelected = isSelected,
            onClick = handlers.getValue(item),
        )
    }

    private fun lotsFeedTapped() {
        currentSelectedTab.value = BottomNavigationItem.LOTS_FEED
        bottomTabsNavigator.openLotsFeedBackstack()
    }

    private fun favouritesTapped() {
        if (authorizationStorage.isAuthorized()) {
            openFavorites()
        } else {
            mainScreenNavigator.openAuthorization()
            viewModelScope.launch {
                val success = authorizationEventsEmitter.waitForAuthorization()
                if (success) {
                    openFavorites()
                }
            }
        }
    }

    private fun openFavorites() {
        currentSelectedTab.value = BottomNavigationItem.FAVORITES
        bottomTabsNavigator.openFavoritesBackstack()
    }

    private fun createLotTapped() {
        currentSelectedTab.value = BottomNavigationItem.CREATE_LOT
    }

    private fun messagesTapped() {
        currentSelectedTab.value = BottomNavigationItem.CHATS
    }

    private fun profileTapped() {
        currentSelectedTab.value = BottomNavigationItem.PROFILE
    }
}