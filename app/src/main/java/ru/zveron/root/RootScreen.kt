package ru.zveron.root

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import ru.zveron.appyx.modal.BottomSheetStateHolder
import ru.zveron.appyx.modal.Modal
import ru.zveron.appyx.modal.activeElement
import ru.zveron.appyx.modal.operation.dismiss
import ru.zveron.appyx.modal.operation.show
import ru.zveron.authorization.phone.RootPhoneNode
import ru.zveron.authorization.socials_sheet.SocialsSheetScreen
import ru.zveron.design.components.BottomSheet
import ru.zveron.main_screen.MainScreen
import ru.zveron.main_screen.MainScreenNavigator

class RootScreen(
    buildContext: BuildContext,
    private val backStack: BackStack<RootScreenNavTarget> = BackStack(
        initialElement = RootScreenNavTarget.MainPage,
        savedStateMap = buildContext.savedStateMap,
    ),
    private val modal: Modal<RootScreenNavTarget> = Modal(
        savedStateMap = buildContext.savedStateMap,
    ),
) : ParentNode<RootScreenNavTarget>(
    buildContext = buildContext,
    navModel = backStack + modal,
), MainScreenNavigator, BottomSheetStateHolder {
    private val activeModalElementFlow: Flow<RootScreenNavTarget?> =
        modal.elements.map { it.activeElement }


    override fun resolve(navTarget: RootScreenNavTarget, buildContext: BuildContext): Node {
        return when (navTarget) {
            RootScreenNavTarget.MainPage -> MainScreen(buildContext, this)
            RootScreenNavTarget.AuthorizationBottomSheet -> SocialsSheetScreen(buildContext) {
                backStack.push(RootScreenNavTarget.PhoneAuthorization)
            }

            RootScreenNavTarget.PhoneAuthorization -> RootPhoneNode(buildContext)
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun View(modifier: Modifier) {
        val shouldBlockSheetStateHolder = shouldBlockBottomSheet.collectAsState(initial = false)

        val sheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden
        ) {
            if (shouldBlockSheetStateHolder.value && it == ModalBottomSheetValue.Hidden) {
                false
            } else {
                it != ModalBottomSheetValue.HalfExpanded
            }
        }

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
                            sheetState.hide()
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

    override val shouldBlockBottomSheet: Flow<Boolean> = modal.screenState.flatMapLatest { screenState ->
        val bottomNavigationModeHolder = screenState.onScreen
            .map { childOrCreate(it.key).nodeOrNull }
            .filterIsInstance<BottomSheetStateHolder>()
            .takeIf { it.isNotEmpty() }
            ?.lastOrNull()

        bottomNavigationModeHolder?.shouldBlockBottomSheet ?: flowOf(false)
    }
}