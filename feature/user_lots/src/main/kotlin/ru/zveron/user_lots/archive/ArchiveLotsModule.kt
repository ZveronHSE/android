package ru.zveron.user_lots.archive

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val archiveLotsModule = module {
    scope<ArchiveLotsComponent> {
        viewModelOf(::ArchiveLotsViewModel)
    }
}