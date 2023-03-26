package ru.zveron.main_screen.bottom_navigation.favorites_backstack

import com.bumble.appyx.core.plugin.Destroyable
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

class FavoritesBackstackComponent: Destroyable, KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }

    override fun destroy() {
        closeScope()
    }
}