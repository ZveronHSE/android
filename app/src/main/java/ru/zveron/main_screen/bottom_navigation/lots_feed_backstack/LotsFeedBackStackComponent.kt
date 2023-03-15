package ru.zveron.main_screen.bottom_navigation.lots_feed_backstack

import com.bumble.appyx.core.plugin.Destroyable
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.component.get
import org.koin.core.scope.Scope
import ru.zveron.lots_feed.choose_item.ChooseItemHolder
import ru.zveron.lots_feed.choose_item.ChooseItemItemProvider

class LotsFeedBackStackComponent: Destroyable, KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }

    override fun destroy() {
        closeScope()
    }

    fun getBackPlugin(): LotsFeedBackPlugin {
        return get()
    }

    fun getChoseeItemItemProvider(): ChooseItemItemProvider {
        return scope.get<ChooseItemHolder>().currentItemItemProvider
    }
}