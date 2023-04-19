package ru.zveron.root

import com.bumble.appyx.core.plugin.Destroyable
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope
import ru.zveron.choose_item.ChooseItemHolder

class RootScreenComponent: KoinScopeComponent, Destroyable {
    override val scope: Scope by lazy { createScope(this) }

    override fun destroy() {
        closeScope()
    }

    fun getChooseItemItemProvider(): ChooseItemHolder {
        return scope.get()
    }
}