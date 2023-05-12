package ru.zveron.personal_profile.profile_preview

import ru.zveron.personal_profile.ProfileUiInfo

interface PersonalProfileNavigator {
    fun reattachMainScreen()

    fun editProfile(profileUiInfo: ProfileUiInfo)
}