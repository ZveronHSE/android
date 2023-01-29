package ru.zveron.authorization.phone

import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.authorization.phone.formatting.PhoneFormatter
import ru.zveron.authorization.phone.password.passwordModule
import ru.zveron.authorization.phone.phone_input.phoneInputModule
import ru.zveron.authorization.phone.registration.registrationModule
import ru.zveron.authorization.phone.sms_code.smsCodeModule

val rootPhoneModule = module {
    scope<RootPhoneNode> {
        scopedOf(::PhoneFormatter)
    }

    includes(
        passwordModule,
        phoneInputModule,
        smsCodeModule,
        registrationModule,
    )
}