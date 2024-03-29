package ru.zveron.create_lot.root

import com.bumble.appyx.core.plugin.Destroyable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.zveron.choose_item.ChooseItemItemProvider
import ru.zveron.create_lot.CreateLotScopeDelegate
import ru.zveron.create_lot.categories_step.CategoriesItemProvider
import ru.zveron.create_lot.categories_step.CategoriesStepNavigator
import ru.zveron.create_lot.details_step.domain.ParametersItemProviderFactory
import ru.zveron.create_lot.domain.ShouldInputGenderInteractor
import ru.zveron.create_lot.gender_step.GenderItemProvider
import ru.zveron.create_lot.gender_step.GenderStepNavigator
import ru.zveron.create_lot.lot_form_step.LotFormItemProvider
import ru.zveron.create_lot.lot_form_step.LotFormStepNavigator

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

    fun getLotFormItemProvider(lotFormStepNavigator: LotFormStepNavigator): LotFormItemProvider {
        return scope.get(parameters = { parametersOf(lotFormStepNavigator) })
    }

    fun getParametersItemProvider(parameterId: Int): ChooseItemItemProvider {
        return scope.get<ParametersItemProviderFactory>().createItemProvider(parameterId)
    }

    fun getShouldInputGenderInteractor(): ShouldInputGenderInteractor {
        return scope.get()
    }

    fun getGenderItemProvider(genderStepNavigator: GenderStepNavigator): GenderItemProvider {
        return scope.get(parameters = { parametersOf(genderStepNavigator) })
    }
}