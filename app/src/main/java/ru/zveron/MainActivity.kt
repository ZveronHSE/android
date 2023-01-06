package ru.zveron

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.core.integration.NodeHost
import com.bumble.appyx.core.integrationpoint.NodeActivity
import com.bumble.appyx.core.integrationpoint.NodeComponentActivity
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.root.RootScreen

class MainActivity : NodeComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZveronTheme {
                NodeHost(integrationPoint = appyxIntegrationPoint) {
                    RootScreen(buildContext = it)
                }
            }
        }
    }
}

