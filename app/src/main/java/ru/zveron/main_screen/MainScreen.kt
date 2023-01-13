package ru.zveron.main_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import ru.zveron.BuildConfig
import ru.zveron.R

class MainScreen(
    buildContext: BuildContext,
    private val mainScreenNavigator: MainScreenNavigator,
) : Node(buildContext = buildContext) {
    @Composable
    override fun View(modifier: Modifier) {
        Screen(modifier = modifier) {
            mainScreenNavigator.openAuthorization()
        }
    }

    @Composable
    private fun Screen(
        modifier: Modifier = Modifier,
        onButtonClick: () -> Unit = {},
    ) {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colors.background
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = onButtonClick,
                ) {
                    Text(text = stringResource(id = R.string.registration_button))
                }

                Spacer(Modifier.height(8.dp))

                Text(text = BuildConfig.host)
            }
        }
    }
}