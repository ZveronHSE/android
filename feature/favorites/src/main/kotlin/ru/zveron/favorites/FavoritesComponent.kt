package ru.zveron.favorites

import com.bumble.appyx.core.plugin.Destroyable
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

class FavoritesComponent: KoinScopeComponent, Destroyable {
    override val scope: Scope by lazy { createScope(this) }

    override fun destroy() {
        closeScope()
    }
}