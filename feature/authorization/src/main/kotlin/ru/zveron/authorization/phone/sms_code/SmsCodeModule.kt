package ru.zveron.authorization.phone.sms_code

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.authorization.phone.phone_input.data.PhoneSendRepository
import ru.zveron.authorization.phone.sms_code.data.CheckCodeRepository
import ru.zveron.authorization.phone.sms_code.deps.SmsCodeDeps
import ru.zveron.authorization.phone.sms_code.domain.CheckCodeInteractor
import ru.zveron.authorization.phone.sms_code.ui.SmsCodeViewModel

internal val smsCodeModule = module {
    scope<SmsCodeComponent> {
        scopedOf(::CheckCodeRepository)

        scopedOf(::CheckCodeInteractor)

        scopedOf(::PhoneSendRepository)

        viewModel {params ->
            val deps = params.get<SmsCodeDeps>()
            SmsCodeViewModel(deps.navigator, get(), deps.phoneNumber, deps.sessionId, get(), get())
        }
    }
}