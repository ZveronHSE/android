package ru.zveron.user_profile

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.user_profile.data.UserProfileRepository
import ru.zveron.user_profile.ui.UserProfileViewModel

val userProfileModule = module {
    scope<UserProfileComponent> {
        viewModelOf(::UserProfileViewModel)
        scopedOf(::UserProfileRepository)
    }
}