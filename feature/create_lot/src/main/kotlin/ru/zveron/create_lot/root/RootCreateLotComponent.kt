package ru.zveron.create_lot.root

import com.bumble.appyx.core.plugin.Destroyable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.zveron.create_lot.CreateLotScopeDelegate
import ru.zveron.categories.CategoriesItemProvider
import ru.zveron.categories.CategoriesStepNavigator

internal class RootCreateLotComponent: KoinScopeComponent, Destroyable, CreateLotScopeDelegate {
    override val scope: Scope by lazy { createScope(this) }

    override val coroutineScope = CoroutineScope(SupervisorJob())

    override fun destroy() {
        closeScope()
        coroutineScope.cancel()
    }

    fun getCategoriesItemProvider(categoriesStepNavigator: CategoriesStepNavigator): CategoriesItemProvider {
        return scope.get(parameters = { parametersOf(categoriesStepNavigator) })
    }
}