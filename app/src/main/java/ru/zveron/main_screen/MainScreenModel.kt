package ru.zveron.main_screen

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val mainScreenModule = module {
    scope<MainScreenComponent> {
        viewModelOf(::MainScreenViewModel)
    }
}