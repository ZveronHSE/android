package ru.zveron.authorization.socials_sheet

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node

class SocialsSheetScreen(
    buildContext: BuildContext,
): Node(buildContext = buildContext) {
    @Composable
    override fun View(modifier: Modifier) {
        Text("Hello authorizaion", modifier = modifier.padding(68.dp))
    }
}