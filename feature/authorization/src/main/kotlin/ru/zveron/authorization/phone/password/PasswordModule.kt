package ru.zveron.authorization.phone.password

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create
import ru.zveron.authorization.phone.password.data.PasswordApi
import ru.zveron.authorization.phone.password.data.PasswordRepository
import ru.zveron.authorization.phone.password.ui.PasswordViewModel

internal val passwordModule = module {
    single<PasswordApi> {
        val retrofit = get<Retrofit>()
        retrofit.create()
    }
    singleOf(::PasswordRepository)

    viewModelOf(::PasswordViewModel)
}