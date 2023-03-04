package ru.zveron.lots_feed.choose_item

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node

class ChooseItemNode(
    buildContext: BuildContext,
    val items: List<Pair<Int, String>>,
    val title: String,
    val onItemPicked: (Int) -> Unit,
): Node(buildContext) {
    @Composable
    override fun View(modifier: Modifier) {
        ChooseItemScreen(
            items = items,
            title = title,
            modifier = modifier,
            onItemClick = onItemPicked,
            onBackClicked = ::navigateUp,
        )
    }
}