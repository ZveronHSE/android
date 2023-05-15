package ru.zveron.user_lots.active

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val activeLotsModule = module {
    scope<ActiveLotsComponent> {
        viewModelOf(::ActiveLotsViewModel)
    }
}