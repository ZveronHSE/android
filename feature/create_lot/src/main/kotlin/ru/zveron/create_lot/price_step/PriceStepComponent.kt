package ru.zveron.create_lot.price_step

import com.bumble.appyx.core.plugin.Destroyable
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

internal class PriceStepComponent: KoinScopeComponent, Destroyable {
    override val scope: Scope by lazy { createScope(this) }

    override fun destroy() {
        closeScope()
    }
}