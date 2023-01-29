package ru.zveron.authorization.phone.registration

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.zveron.authorization.phone.registration.data.RegistrationApi
import ru.zveron.authorization.phone.registration.data.RegistrationRepository
import ru.zveron.authorization.phone.registration.domain.RegistrationInteractor
import ru.zveron.authorization.phone.registration.ui.RegistrationViewModel

internal val registrationModule = module {
    single {
        val retrofit = get<Retrofit>()
        retrofit.create(RegistrationApi::class.java)
    }

    singleOf(::RegistrationRepository)
    singleOf(::RegistrationInteractor)

    viewModel { params ->
        val phone = params.get<String>()
        RegistrationViewModel(phone, get())
    }
}