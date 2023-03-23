package ru.zveron.authorization.phone.password

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.authorization.phone.password.data.PasswordRepository
import ru.zveron.authorization.phone.password.domain.PasswordLoginInteractor
import ru.zveron.authorization.phone.password.ui.PasswordViewModel

internal val passwordModule = module {
    scope<PasswordInputComponent> {
        scopedOf(::PasswordRepository)
        scopedOf(::PasswordLoginInteractor)

        viewModelOf(::PasswordViewModel)
    }
}