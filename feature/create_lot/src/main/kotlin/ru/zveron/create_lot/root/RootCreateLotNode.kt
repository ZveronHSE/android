package ru.zveron.create_lot.root

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import kotlinx.parcelize.Parcelize
import ru.zveron.appyx.viewmodel.ViewModelParentNode
import ru.zveron.categories.CategoriesStepNavigator
import ru.zveron.choose_item.ChooseItemNode
import ru.zveron.create_lot.R
import ru.zveron.create_lot.general_step.GeneralStepNavigator
import ru.zveron.create_lot.general_step.GeneralStepNode
import ru.zveron.design.resources.ZveronText

class RootCreateLotNode private constructor(
    buildContext: BuildContext,
    private val component: RootCreateLotComponent = RootCreateLotComponent(),
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.GeneralStep,
        savedStateMap = buildContext.savedStateMap,
    )
) : ViewModelParentNode<RootCreateLotNode.NavTarget>(
    buildContext = buildContext,
    navModel = backStack,
    plugins = listOf(component),
), GeneralStepNavigator, CategoriesStepNavigator {
    private val categoriesItemProvider by lazy {
        component.getCategoriesItemProvider(this)
    }

    constructor(buildContext: BuildContext) : this(
        buildContext,
        RootCreateLotComponent(),
        BackStack(
            initialElement = NavTarget.GeneralStep,
            savedStateMap = buildContext.savedStateMap,
        ),
    )

    sealed class NavTarget : Parcelable {
        @Parcelize
        object GeneralStep : NavTarget()

        @Parcelize
        object CategoriesStep : NavTarget()
    }

    override fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            NavTarget.GeneralStep -> GeneralStepNode(
                buildContext,
                component.scope,
                this,
            )

            NavTarget.CategoriesStep -> ChooseItemNode(
                buildContext,
                ZveronText.RawResource(R.string.categories_title),
                categoriesItemProvider,
            )
        }
    }

    @Composable
    override fun View(modifier: Modifier) {

        Children(
            navModel = backStack,
            modifier = modifier,
        )
    }

    override fun goToNextStep() {
        categoriesItemProvider.launchChildCategoryLoad()
        backStack.push(NavTarget.CategoriesStep)
    }

    override fun completeCategoriesStep() {
        TODO("Not yet implemented")
    }
}