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
import ru.zveron.create_lot.categories_step.CategoriesStepNavigator
import ru.zveron.choose_item.ChooseItemNode
import ru.zveron.create_lot.R
import ru.zveron.create_lot.address_channels_step.AddressChannelsStepNavigator
import ru.zveron.create_lot.address_channels_step.AddressChannelsStepNode
import ru.zveron.create_lot.details_step.DetailsStepNavigator
import ru.zveron.create_lot.details_step.DetailsStepNode
import ru.zveron.create_lot.gender_step.GenderStepNavigator
import ru.zveron.create_lot.general_step.GeneralStepNavigator
import ru.zveron.create_lot.general_step.GeneralStepNode
import ru.zveron.create_lot.lot_form_step.LotFormStepNavigator
import ru.zveron.create_lot.price_step.PriceStepNavigator
import ru.zveron.create_lot.price_step.PriceStepNode
import ru.zveron.design.resources.ZveronText

class RootCreateLotNode private constructor(
    buildContext: BuildContext,
    private val navigator: RootCreateLotNavigator,
    private val component: RootCreateLotComponent = RootCreateLotComponent(),
    private val backStack: BackStack<NavTarget> = BackStack(
        initialElement = NavTarget.GeneralStep,
        savedStateMap = buildContext.savedStateMap,
    )
) : ViewModelParentNode<RootCreateLotNode.NavTarget>(
    buildContext = buildContext,
    navModel = backStack,
    plugins = listOf(component),
), GeneralStepNavigator, CategoriesStepNavigator, LotFormStepNavigator, DetailsStepNavigator,
    PriceStepNavigator, AddressChannelsStepNavigator, GenderStepNavigator {
    private val categoriesItemProvider by lazy {
        component.getCategoriesItemProvider(this)
    }

    private val lotFormItemProvider by lazy {
        component.getLotFormItemProvider(this)
    }

    private val genderItemProvider by lazy {
        component.getGenderItemProvider(this)
    }

    constructor(buildContext: BuildContext, navigator: RootCreateLotNavigator) : this(
        buildContext,
        navigator,
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

        @Parcelize
        object LotFormStep : NavTarget()

        @Parcelize
        object DetailsStep : NavTarget()

        @Parcelize
        data class PickParameterValue(val id: Int, val parameterName: String) : NavTarget()

        @Parcelize
        object PriceStep : NavTarget()

        @Parcelize
        object AddressChannelsStep: NavTarget()

        @Parcelize
        object GenderStep: NavTarget()
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
                removeOnItemPick = false,
            )

            NavTarget.LotFormStep -> ChooseItemNode(
                buildContext,
                ZveronText.RawResource(R.string.lot_form_title),
                lotFormItemProvider,
                removeOnItemPick = false,
            )

            NavTarget.DetailsStep -> DetailsStepNode(
                buildContext,
                component.scope,
                this,
            )

            is NavTarget.PickParameterValue -> ChooseItemNode(
                buildContext,
                ZveronText.RawString(navTarget.parameterName),
                component.getParametersItemProvider(navTarget.id),
                removeOnItemPick = true,
            )

            NavTarget.PriceStep -> PriceStepNode(
                buildContext,
                component.scope,
                this,
            )

            NavTarget.AddressChannelsStep -> AddressChannelsStepNode(
                buildContext,
                component.scope,
                this,
            )

            NavTarget.GenderStep -> ChooseItemNode(
                buildContext,
                ZveronText.RawResource(R.string.gender_title),
                genderItemProvider,
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
        if (component.getShouldInputGenderInteractor().shouldSelectGender()) {
            backStack.push(NavTarget.GenderStep)
        } else {
            lotFormItemProvider.launchLotFormLoad()
            backStack.push(NavTarget.LotFormStep)
        }
    }

    override fun completeLotFormStep() {
        backStack.push(NavTarget.DetailsStep)
    }

    override fun completeDetailsStep() {
        backStack.push(NavTarget.PriceStep)
    }

    override fun pickParameterValue(id: Int, parameterName: String) {
        backStack.push(NavTarget.PickParameterValue(id, parameterName))
    }

    override fun completePriceStep() {
        backStack.push(NavTarget.AddressChannelsStep)
    }

    override fun completeAddressChannelsStep(id: Long) {
        navigateUp()
        navigator.openLot(id)
    }

    override fun completeGenderStep() {
        lotFormItemProvider.launchLotFormLoad()
        backStack.push(NavTarget.LotFormStep)
    }
}