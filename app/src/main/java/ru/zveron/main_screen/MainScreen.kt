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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import ru.zveron.BuildConfig
import ru.zveron.R
import ru.zveron.design.components.BottomNavigation
import ru.zveron.design.components.BottomNavigationItem
import ru.zveron.design.resources.ZveronImage
import ru.zveron.design.resources.ZveronText

class MainScreen(
    buildContext: BuildContext,
    private val mainScreenNavigator: MainScreenNavigator,
) : Node(buildContext = buildContext) {

    private val bottomNavigationItems = listOf(
        BottomNavigationItem(
            image = ZveronImage.ResourceImage(R.drawable.home_unselected),
            title = ZveronText.RawResource(R.string.lots_feed_label),
            isSelected = false,
            onClick = {},
        ),
        BottomNavigationItem(
            image = ZveronImage.ResourceImage(R.drawable.favourites_unselected),
            title = ZveronText.RawResource(R.string.favorites_label),
            isSelected = false,
            onClick = {},
        ),
        BottomNavigationItem(
            image = ZveronImage.ResourceImage(R.drawable.add_lot_unselected),
            title = ZveronText.RawResource(R.string.create_lot_label),
            isSelected = false,
            onClick = {},
        ),
        BottomNavigationItem(
            image = ZveronImage.ResourceImage(R.drawable.chat_unselected),
            title = ZveronText.RawResource(R.string.messages_label),
            isSelected = false,
            onClick = {},
        ),
        BottomNavigationItem(
            image = ZveronImage.ResourceImage(R.drawable.profile_unselected),
            title = ZveronText.RawResource(R.string.profile_label),
            isSelected = false,
            onClick = {},
        ),
    )

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
                    items = bottomNavigationItems,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(start = 16.dp, end = 16.dp, bottom = 34.dp),
                )
            }

        }
    }
}