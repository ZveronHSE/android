package ru.zveron.personal_profile.profile_preview

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.zveron.personal_profile.profile_preview.ui.PersonalProfileViewModel

val personalProfileModule = module {
     scope<PersonalProfileComponent> {
         viewModelOf(::PersonalProfileViewModel)
     }
}