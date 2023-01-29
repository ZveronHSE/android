package ru.zveron

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.bumble.appyx.core.integration.NodeHost
import ru.zveron.appyx.viewmodel.ViewModelNodeComponentActivity
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.root.RootScreen

class MainActivity : ViewModelNodeComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ZveronTheme {
                NodeHost(integrationPoint = appyxIntegrationPoint) {
                    RootScreen(buildContext = it)
                }
            }
        }
    }
}

