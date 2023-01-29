package ru.zveron.authorization.phone.phone_input

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.zveron.authorization.phone.phone_input.data.PhoneSendApi
import ru.zveron.authorization.phone.phone_input.data.PhoneSendRepository
import ru.zveron.authorization.phone.phone_input.ui.PhoneInputViewModel

internal val phoneInputModule = module {
    scope<PhoneInputComponent> {
        scoped {
            val retrofit = get<Retrofit>()
            retrofit.create(PhoneSendApi::class.java)
        }

        scopedOf(::PhoneSendRepository)

        viewModelOf(::PhoneInputViewModel)
    }
}