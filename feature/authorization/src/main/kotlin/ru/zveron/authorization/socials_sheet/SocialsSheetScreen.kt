package ru.zveron.authorization.socials_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import ru.zveron.authorization.R
import ru.zveron.design.components.ActionButton
import ru.zveron.design.components.BottomSheet
import ru.zveron.design.components.LoginButton
import ru.zveron.design.theme.GoogleColors
import ru.zveron.design.theme.VkColors

class SocialsSheetScreen(
    buildContext: BuildContext,
): Node(buildContext = buildContext) {
    @Composable
    override fun View(modifier: Modifier) {
        SocialsScreen(modifier)
    }
}

@Composable
private fun SocialsScreen(
    modifier: Modifier = Modifier,
    onGoogleButtonClick: () -> Unit = {},
    onVkButtonClick: () -> Unit = {},
    onPhoneButtonClick: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterHorizontally),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            LoginButton(loginButtonColors = GoogleColors, onClick = onGoogleButtonClick)

            LoginButton(loginButtonColors = VkColors, onClick = onVkButtonClick)
        }

        Spacer(Modifier.height(24.dp))

        ActionButton(
            onClick = onPhoneButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(stringResource(R.string.login_with_number))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
fun PreviewSocials() {
    BottomSheet {
        SocialsScreen()
    }
}