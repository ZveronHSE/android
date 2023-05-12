package ru.zveron.personal_profile.profile_preview

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.personal_profile.profile_preview.data.DeleteAccountRepository
import ru.zveron.personal_profile.profile_preview.data.LogoutRepository
import ru.zveron.personal_profile.profile_preview.data.PersonalProfileRepository
import ru.zveron.personal_profile.profile_preview.ui.PersonalProfileViewModel

val personalProfileModule = module {
     scope<PersonalProfileComponent> {
         viewModelOf(::PersonalProfileViewModel)

         scopedOf(::PersonalProfileRepository)
         scopedOf(::LogoutRepository)
         scopedOf(::DeleteAccountRepository)
     }
}