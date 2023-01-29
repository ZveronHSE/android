package ru.zveron.authorization.phone.registration

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.zveron.authorization.phone.registration.data.RegistrationApi
import ru.zveron.authorization.phone.registration.data.RegistrationRepository
import ru.zveron.authorization.phone.registration.domain.RegistrationInteractor
import ru.zveron.authorization.phone.registration.ui.RegistrationViewModel

internal val registrationModule = module {
    scope<RegistrationComponent> {
        scoped {
            val retrofit = get<Retrofit>()
            retrofit.create(RegistrationApi::class.java)
        }

        scopedOf(::RegistrationRepository)
        scopedOf(::RegistrationInteractor)

        viewModel { params ->
            val phone = params.get<String>()
            RegistrationViewModel(phone, get())
        }
    }
}