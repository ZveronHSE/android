package ru.zveron.authorization.phone.sms_code

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.zveron.authorization.phone.formatting.PhoneFormatter
import ru.zveron.authorization.phone.sms_code.data.CheckCodeApi
import ru.zveron.authorization.phone.sms_code.data.CheckCodeRepository
import ru.zveron.authorization.phone.sms_code.deps.SmsCodeDeps
import ru.zveron.authorization.phone.sms_code.domain.CheckCodeInteractor
import ru.zveron.authorization.phone.sms_code.ui.SmsCodeViewModel

internal val smsCodeModule = module {
    singleOf(::PhoneFormatter)

    singleOf(::CheckCodeRepository)

    singleOf(::CheckCodeInteractor)

    single {
        val retrofit = get<Retrofit>()
        retrofit.create(CheckCodeApi::class.java)
    }

    viewModel {params ->
        val deps = params.get<SmsCodeDeps>()
        SmsCodeViewModel(deps.navigator, get(), deps.phoneNumber, get(), get())
    }
}