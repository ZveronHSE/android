package ru.zveron.create_lot

import com.bumble.appyx.core.plugin.Destroyable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope

class RootCreateLotComponent: KoinScopeComponent, Destroyable, CreateLotScopeDelegate {
    override val scope: Scope by lazy { createScope(this) }

    override val coroutineScope = CoroutineScope(SupervisorJob())

    override fun destroy() {
        closeScope()
        coroutineScope.cancel()
    }
}