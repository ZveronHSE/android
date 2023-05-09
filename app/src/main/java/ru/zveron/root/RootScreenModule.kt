package ru.zveron.root

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.binds
import org.koin.dsl.module
import ru.zveron.platform.dialog.DialogManager

val rootScreenModule = module {
    singleOf(::RootDialogManagerImpl) binds arrayOf(
        DialogManager::class,
        RootDialogStateHolder::class,
    )
}