package ru.zveron.authorization.phone.registration

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.authorization.phone.registration.data.RegistrationRepository
import ru.zveron.authorization.phone.registration.domain.RegistrationInteractor
import ru.zveron.authorization.phone.registration.ui.RegistrationViewModel

internal val registrationModule = module {
    scope<RegistrationComponent> {
        scopedOf(::RegistrationRepository)
        scopedOf(::RegistrationInteractor)

        viewModel { params ->
            val phone = params.get<String>()
            RegistrationViewModel(phone, get())
        }
    }
}