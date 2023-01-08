package ru.zveron.authorization.phone.phone_input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import ru.zveron.authorization.R
import ru.zveron.design.components.ActionButton
import ru.zveron.design.inputs.PhoneTextField
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.enabledButtonGradient

class PhoneInputNode(
    buildContext: BuildContext,
    private val navigateToPassword: () -> Unit,
    private val navigateToSms: (String) -> Unit,
) : Node(buildContext = buildContext) {
    private val textState = mutableStateOf("")

    @Composable
    override fun View(modifier: Modifier) {
        val (text, changeText) = remember { textState }

        PhoneInput(
            text = text,
            onTextChanged = changeText,
            modifier = modifier,
            onBackClicked = ::navigateUp,
            onContinueClicked = ::onContinueClicked,
            onPasswordClicked = navigateToPassword,
        )
    }

    private fun onContinueClicked() {
        navigateToSms.invoke(textState.value)
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun PhoneInput(
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onContinueClicked: () -> Unit = {},
    onPasswordClicked: () -> Unit = {},
) {
    Column(modifier = modifier.fillMaxSize()) {
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp, start = 4.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_close),
                // TODO: fix accessibility
                contentDescription = null,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Text(
                stringResource(R.string.phone_number_label),
                style = TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 34.sp,
                    letterSpacing = 0.36.sp,
                    fontWeight = FontWeight.Medium,
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                stringResource(R.string.sms_send_label),
                style = TextStyle(
                    color = Color(0xFF666770),
                    fontSize = 13.sp,
                    lineHeight = 15.23.sp,
                    fontWeight = FontWeight.Normal,
                )
            )

            Spacer(Modifier.height(24.dp))

            PhoneTextField(
                countryCodePrefix = stringResource(R.string.country_code_prefix),
                text = text,
                onTextChanged = onTextChanged,
                textStyle = TextStyle(
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    letterSpacing = 0.36.sp,
                    fontWeight = FontWeight.Medium,
                )
            )
        }

        ActionButton(
            onClick = onContinueClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(
                stringResource(R.string.sms_button_label),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 18.75.sp,
                    fontWeight = FontWeight.Normal,
                )
            )
        }

        Spacer(Modifier.height(8.dp))

        TextButton(
            onClick = onPasswordClicked,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp),
        ) {
            Text(
                stringResource(R.string.login_with_password),
                style = TextStyle(
                    brush = enabledButtonGradient,
                    fontSize = 14.sp,
                    lineHeight = 16.41.sp,
                    fontWeight = FontWeight.Light,
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PhoneInputPreview() {
    ZveronTheme {
        val (text, changeText) = remember {
            mutableStateOf("")
        }

        PhoneInput(text, changeText, modifier = Modifier.fillMaxSize())
    }
}