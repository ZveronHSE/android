package ru.zveron.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import ru.zveron.BuildConfig
import ru.zveron.R
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.design.components.BottomNavigation
import ru.zveron.design.components.BottomNavigationItem

internal class MainScreen(
    buildContext: BuildContext,
    private val mainScreenNavigator: MainScreenNavigator,
    private val mainScreenComponent: MainScreenComponent = MainScreenComponent(),
) : ViewModelNode(
    buildContext = buildContext,
    plugins = listOf(mainScreenComponent),
) {

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<MainScreenViewModel>(
            scope = mainScreenComponent.scope,
            viewModelStoreOwner = this,
        )

        val items by viewModel.state.collectAsState()

        Screen(items, modifier = modifier) {
            mainScreenNavigator.openAuthorization()
        }
    }

    @Composable
    private fun Screen(
        items: List<BottomNavigationItem>,
        modifier: Modifier = Modifier,
        onButtonClick: () -> Unit = {},
    ) {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colors.background
        ) {
            Box(Modifier.fillMaxSize()) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Button(
                        onClick = onButtonClick,
                    ) {
                        Text(text = stringResource(id = R.string.registration_button))
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(text = BuildConfig.host)
                }

                BottomNavigation(
                    items = items,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 16.dp, end = 16.dp, bottom = 34.dp),
                )
            }

        }
    }
}