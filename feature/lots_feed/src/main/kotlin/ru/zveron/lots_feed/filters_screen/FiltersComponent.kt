package ru.zveron.lots_feed.filters_screen

import com.bumble.appyx.core.plugin.Destroyable
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope
import ru.zveron.lots_feed.filters_screen.domain.PassDataToFeedInteractor

class FiltersComponent: KoinScopeComponent, Destroyable {
    override val scope: Scope by lazy { createScope(this) }

    override fun destroy() {
        closeScope()
    }

    fun getPassDataToFeedInteractor(): PassDataToFeedInteractor {
        return scope.get()
    }
}