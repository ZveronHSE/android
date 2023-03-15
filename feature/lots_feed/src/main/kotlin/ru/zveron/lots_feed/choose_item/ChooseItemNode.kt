package ru.zveron.lots_feed.choose_item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.zveron.appyx.bottom_navigation.BottomNavigationMode
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.design.resources.ZveronText

class ChooseItemNode(
    buildContext: BuildContext,
    val title: ZveronText,
    val chooseItemItemProvider: ChooseItemItemProvider
) : ViewModelNode(buildContext) {
    @Composable
    override fun View(modifier: Modifier) {
        val uiState by chooseItemItemProvider.uiState.collectAsState()

        val itemClickHandler = remember {
            { id: Int ->
                chooseItemItemProvider.itemPicked(id)
                navigateUp()
            }
        }

        ChooseItemScreen(
            uiState = uiState,
            title = title,
            modifier = modifier,
            onItemClick = itemClickHandler,
            onBackClicked = ::navigateUp,
        )
    }

    override val bottomNavigationMode: Flow<BottomNavigationMode> =
        flowOf(BottomNavigationMode.HIDE_BOTTOM_BAR)
}