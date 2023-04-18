package ru.zveron.user_lots

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module
import ru.zveron.user_lots.data.MockUserLotsDataSource
import ru.zveron.user_lots.data.UserLotsDataSource
import ru.zveron.user_lots.data.UserLotsRepository
import ru.zveron.user_lots.domain.GetUserLotsInteractor
import ru.zveron.user_lots.ui.UserLotsViewModel

val userLotsModule = module {
    scope<UserLotsComponent> {
        viewModelOf(::UserLotsViewModel)
        scopedOf(::GetUserLotsInteractor)
        scopedOf(::UserLotsRepository)
        scoped<UserLotsDataSource> {
            MockUserLotsDataSource()
        }
    }
}