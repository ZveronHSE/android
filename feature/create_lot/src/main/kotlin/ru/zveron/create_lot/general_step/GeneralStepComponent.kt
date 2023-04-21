package ru.zveron.create_lot.general_step

import com.bumble.appyx.core.plugin.Destroyable
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

internal class GeneralStepComponent: KoinScopeComponent, Destroyable {
    override val scope: Scope by lazy { createScope(this) }

    override fun destroy() {
        closeScope()
    }
}