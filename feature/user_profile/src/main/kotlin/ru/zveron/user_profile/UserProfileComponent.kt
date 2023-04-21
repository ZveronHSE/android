package ru.zveron.user_profile

import com.bumble.appyx.core.plugin.Destroyable
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

class UserProfileComponent: KoinScopeComponent, Destroyable {
    override val scope: Scope by lazy { createScope(this) }

    override fun destroy() {
        closeScope()
    }
}