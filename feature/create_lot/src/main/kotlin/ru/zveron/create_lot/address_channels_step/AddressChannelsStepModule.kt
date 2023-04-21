package ru.zveron.create_lot.address_channels_step

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import ru.zveron.create_lot.address_channels_step.ui.AddressChannelsStepViewModel

internal val addressChannelsStepModule = module {
    scope<AddressChannelsStepComponent> {
        viewModelOf(::AddressChannelsStepViewModel)
    }
}