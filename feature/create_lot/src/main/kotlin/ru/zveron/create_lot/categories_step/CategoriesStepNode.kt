package ru.zveron.create_lot.categories_step

import com.bumble.appyx.core.modality.BuildContext
import org.koin.core.scope.Scope
import ru.zveron.appyx.viewmodel.ViewModelNode

internal class CategoriesStepNode(
    buildContext: BuildContext,
    scope: Scope,
    private val component: CategoriesStepComponent = CategoriesStepComponent(),
): ViewModelNode(
    buildContext = buildContext,
    plugins = listOf(component)
) {
    init {
        component.scope.linkTo(scope)
    }
}