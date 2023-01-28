package ru.zveron.authorization.phone

import org.koin.dsl.module
import ru.zveron.authorization.phone.password.passwordModule
import ru.zveron.authorization.phone.phone_input.phoneInputModule
import ru.zveron.authorization.phone.sms_code.smsCodeModule

val rootPhoneModule = module {
    includes(
        passwordModule,
        phoneInputModule,
        smsCodeModule,
    )
}