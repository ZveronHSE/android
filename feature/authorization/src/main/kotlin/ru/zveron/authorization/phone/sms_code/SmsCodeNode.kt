package ru.zveron.authorization.phone.sms_code

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import ru.zveron.authorization.R
import ru.zveron.design.inputs.SmsCodeTxtField
import ru.zveron.design.theme.ZveronTheme

class SmsCodeNode(
    buildContext: BuildContext,
    private val phoneNumber: String,
    private val navigateToPassword: () -> Unit,
    private val navigateToRegistration: () -> Unit,
) : Node(buildContext = buildContext) {
    private val codeState = mutableStateOf("")

    @Composable
    override fun View(modifier: Modifier) {
        val code by remember { codeState }
        SmsCodeInput(
            code = code,
            onCodeChanged = ::onCodeChanged,
            phoneNumber = phoneNumber,
            modifier = modifier,
            onBackClicked = ::navigateUp,
            onPasswordClicked = navigateToPassword,
        )
    }

    private fun onCodeChanged(code: String) {
        codeState.value = code
        // TODO: change condition later
        if (code.length == 5) {
            navigateToRegistration.invoke()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SmsCodeInput(
    code: String,
    onCodeChanged: (String) -> Unit,
    phoneNumber: String,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onPasswordClicked: () -> Unit = {},
) {
    val     unbreakablePhoneNumber by remember(phoneNumber) {
        mutableStateOf(phoneNumber.replace(' ', '\u00A0'))
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
    ) {
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier.padding(top = 16.dp, start = 4.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                // TODO: fix accessibility
                contentDescription = null,
            )
        }

        Spacer(Modifier.height(34.dp))

        Text(
            stringResource(R.string.confirmation_code),
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 34.sp,
                letterSpacing = 0.36.sp,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier.padding(start = 16.dp),
        )

        Spacer(Modifier.height(4.dp))

        Text(
            stringResource(R.string.sms_sent_label, unbreakablePhoneNumber),
            style = TextStyle(
                fontWeight = FontWeight.Light,
                fontSize = 16.sp,
                lineHeight = 18.75.sp,
            ),
            modifier = Modifier.padding(start = 16.dp),
        )

        Spacer(Modifier.height(38.dp))


        SmsCodeTxtField(
            code = code,
            onCodeChanged = onCodeChanged,
            modifier = Modifier.padding(start = 8.dp),
            textStyle = TextStyle(
                fontSize = 18.sp,
                lineHeight = 20.sp,
                letterSpacing = (-0.5).sp,
                fontWeight = FontWeight.SemiBold,
            ),
            dividerColor = Color(0x4D000000),
        )

        Spacer(Modifier.weight(1f))

        TextButton(
            onClick = onPasswordClicked,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.textButtonColors(
                // TODO: replace later with theme value
                contentColor = Color(0xFF18191B)
            )
        ) {
            Text(
                stringResource(R.string.login_with_password),
                style = TextStyle(
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    lineHeight = 16.41.sp,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSmsCode() {
    ZveronTheme {
        val (code, setCode) = remember { mutableStateOf("") }

        SmsCodeInput(
            code = code,
            onCodeChanged = setCode,
            phoneNumber = "+7 (999) 999-99-99",
            modifier = Modifier.fillMaxSize(),
        )
    }
}