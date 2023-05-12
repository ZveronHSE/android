package ru.zveron.personal_profile.edit_profile

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.personal_profile.edit_profile.data.EditProfilePhotoConversionCacheRepository
import ru.zveron.personal_profile.edit_profile.data.EditProfilePhotoStateRepository
import ru.zveron.personal_profile.edit_profile.data.EditProfilePhotoUriRepository
import ru.zveron.personal_profile.edit_profile.data.EditProfileRepository
import ru.zveron.personal_profile.edit_profile.domain.EditProfileInteractor
import ru.zveron.personal_profile.edit_profile.domain.EditProfileUploadAvatarInteractor
import ru.zveron.personal_profile.edit_profile.ui.EditProfileViewModel

val editProfileModule = module {
    scope<EditProfileComponent> {
        viewModelOf(::EditProfileViewModel)
        scopedOf(::EditProfileUploadAvatarInteractor)
        scopedOf(::EditProfilePhotoStateRepository)
        scopedOf(::EditProfilePhotoUriRepository)
        scopedOf(::EditProfilePhotoConversionCacheRepository)
        scopedOf(::EditProfileRepository)
        scopedOf(::EditProfileInteractor)
    }
}