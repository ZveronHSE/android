package ru.zveron.authorization.phone

import org.koin.dsl.module
import ru.zveron.authorization.phone.password.passwordModule

val rootPhoneModule = module {
    includes(passwordModule)
}