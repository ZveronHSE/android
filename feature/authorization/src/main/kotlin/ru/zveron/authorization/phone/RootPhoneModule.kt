package ru.zveron.authorization.phone

import org.koin.dsl.module
import ru.zveron.authorization.phone.password.passwordModule
import ru.zveron.authorization.phone.phone_input.phoneInputModule

val rootPhoneModule = module {
    includes(
        passwordModule,
        phoneInputModule,
    )
}