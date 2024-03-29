package ru.zveron.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TextButton
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.core.children.nodeOrNull
import com.bumble.appyx.core.composable.Children
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.navigation.model.combined.plus
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.core.node.ParentNode
import com.bumble.appyx.navmodel.backstack.BackStack
import com.bumble.appyx.navmodel.backstack.operation.push
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.zveron.appyx.modal.BottomSheetStateHolder
import ru.zveron.appyx.modal.Modal
import ru.zveron.appyx.modal.activeElement
import ru.zveron.appyx.modal.operation.dismiss
import ru.zveron.appyx.modal.operation.show
import ru.zveron.appyx.operations.clearWithNewRoot
import ru.zveron.authorization.phone.RootPhoneNode
import ru.zveron.authorization.socials_sheet.SocialsSheetScreen
import ru.zveron.choose_item.ChooseItemNode
import ru.zveron.create_lot.root.RootCreateLotNavigator
import ru.zveron.create_lot.root.RootCreateLotNode
import ru.zveron.design.components.ActionButton
import ru.zveron.design.components.BottomSheet
import ru.zveron.design.resources.ZveronText
import ru.zveron.design.theme.gray3
import ru.zveron.lot_card.LotCardNavigator
import ru.zveron.lot_card.LotCardNode
import ru.zveron.lot_card.LotCardParams
import ru.zveron.main_screen.MainScreen
import ru.zveron.main_screen.MainScreenNavigator
import ru.zveron.platform.dialog.DialogResult
import ru.zveron.user_profile.UserProfileNode
import ru.zveron.user_profile.UserProfileParams

class RootScreen(
    buildContext: BuildContext,
    private val backStack: BackStack<RootScreenNavTarget> = BackStack(
        initialElement = RootScreenNavTarget.MainPage,
        savedStateMap = buildContext.savedStateMap,
    ),
    private val modal: Modal<RootScreenNavTarget> = Modal(
        savedStateMap = buildContext.savedStateMap,
    ),
    private val component: RootScreenComponent = RootScreenComponent(),
) : ParentNode<RootScreenNavTarget>(
    buildContext = buildContext,
    navModel = backStack + modal,
    plugins = listOf(component),
), MainScreenNavigator, BottomSheetStateHolder, RootCreateLotNavigator, LotCardNavigator {
    private val activeModalElementFlow: Flow<RootScreenNavTarget?> =
        modal.elements.map { it.activeElement }

    private val rootDialogStateHolder by lazy {
        component.getRootDialogScreenStateHolder()
    }

    private val currentItemProvider
        get() = component.getChooseItemItemProvider().currentItemItemProvider


    override fun resolve(navTarget: RootScreenNavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            RootScreenNavTarget.MainPage -> MainScreen(buildContext, this)
            RootScreenNavTarget.AuthorizationBottomSheet -> SocialsSheetScreen(buildContext) {
                backStack.push(RootScreenNavTarget.PhoneAuthorization)
            }

            is RootScreenNavTarget.LotCard -> LotCardNode(buildContext, LotCardParams(navTarget.id), this)
            RootScreenNavTarget.PhoneAuthorization -> RootPhoneNode(buildContext)
            RootScreenNavTarget.CreateLot -> RootCreateLotNode(buildContext, this)
            is RootScreenNavTarget.PickItem -> ChooseItemNode(
                buildContext,
                navTarget.title,
                currentItemProvider,
            )

            is RootScreenNavTarget.Profile -> UserProfileNode(
                buildContext,
                UserProfileParams(navTarget.profileId),
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun View(modifier: Modifier) {
        val currentDialogRequest by rootDialogStateHolder.requests.collectAsState()
        currentDialogRequest?.let { request ->
            AlertDialog(
                onDismissRequest = {
                    request.submitResult(DialogResult.Dismiss)
                },
                buttons = {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
                    ) {
                        ActionButton(
                            onClick = { request.submitResult(DialogResult.Confirm) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            ZveronText(request.params.confirmButtonLabel)
                        }

                        request.params.dismissButtonLabel?.let {
                            TextButton(
                                onClick = { request.submitResult(DialogResult.Dismiss) },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                            ) {
                                ZveronText(it, color = gray3)
                            }
                        }
                    }
                },
                modifier = modifier,
                title = request.params.title?.let {
                    {
                        ZveronText(
                            it,
                            style = TextStyle(
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                lineHeight = (27.6).sp,
                                letterSpacing = (-0.69).sp,
                            ),
                        )
                    }
                },
                text = request.params.message?.let {
                    {
                        ZveronText(
                            it,
                            style = MaterialTheme.typography.body1,
                        )
                    }
                },
            )
        }

        val shouldBlockSheetStateHolder = shouldBlockBottomSheet.collectAsState(initial = false)

        val sheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmValueChange = {
                if (shouldBlockSheetStateHolder.value && it == ModalBottomSheetValue.Hidden) {
                    false
                } else {
                    it != ModalBottomSheetValue.HalfExpanded
                }
            }
        )

        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetContent = { SheetContent(sheetState) },
            sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        ) {
            Children(
                navModel = backStack,
                modifier = Modifier.fillMaxSize()
            ) {
                children<RootScreenNavTarget> { child ->
                    child(
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.systemBars.union(WindowInsets.displayCutout))
                            .fillMaxSize()
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun SheetContent(sheetState: ModalBottomSheetState) {
        val activeElement by activeModalElementFlow.collectAsState(initial = null)

        // fix for this issue - https://stackoverflow.com/questions/68623965/jetpack-compose-modalbottomsheetlayout-throws-java-lang-illegalargumentexception
        if (activeElement == null) {
            Spacer(Modifier.size(1.dp))
        }

        Children(navModel = modal) {
            BottomSheet {
                val scope = rememberCoroutineScope()

                children<RootScreenNavTarget> { child ->
                    var hideCalled by remember(activeElement) { mutableStateOf(false) }

                    LaunchedEffect(activeElement, hideCalled) {
                        val sheetVisibility = snapshotFlow { sheetState.isVisible }
                        sheetVisibility
                            .distinctUntilChanged()
                            .drop(1)
                            .filter { isSheetVisible -> !isSheetVisible }
                            .collect { if (!hideCalled) modal.dismiss() }
                    }

                    LaunchedEffect(activeElement) {
                        if (activeElement != null && !sheetState.isVisible) {
                            sheetState.show()
                        } else if (activeElement == null && sheetState.isVisible) {
                            hideCalled = true
                            scope.launch { sheetState.hide() }
                        }
                    }

                    child(
                        modifier = Modifier.windowInsetsPadding(
                            WindowInsets.navigationBars.union(WindowInsets.ime)
                        )
                    )
                }
            }
        }
    }

    override fun openAuthorization() {
        modal.show(RootScreenNavTarget.AuthorizationBottomSheet)
    }

    override fun openLot(id: Long) {
        backStack.push(RootScreenNavTarget.LotCard(id))
    }

    override fun createLot() {
        backStack.push(RootScreenNavTarget.CreateLot)
    }

    override fun pickItem(title: ZveronText) {
        backStack.push(RootScreenNavTarget.PickItem(title))
    }

    override fun goToSeller(id: Long) {
        backStack.push(RootScreenNavTarget.Profile(id))
    }

    override fun reattachMainScreen() {
        backStack.clearWithNewRoot(RootScreenNavTarget.MainPage)
        modal.dismiss()
    }

    override val shouldBlockBottomSheet: Flow<Boolean> =
        modal.screenState.flatMapLatest { screenState ->
            val bottomNavigationModeHolder = screenState.onScreen
                .map { childOrCreate(it.key).nodeOrNull }
                .filterIsInstance<BottomSheetStateHolder>()
                .takeIf { it.isNotEmpty() }
                ?.lastOrNull()

            bottomNavigationModeHolder?.shouldBlockBottomSheet ?: flowOf(false)
        }
}