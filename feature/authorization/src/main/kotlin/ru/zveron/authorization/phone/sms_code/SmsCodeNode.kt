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
) : Node(buildContext = buildContext) {
    private val codeState = mutableStateOf("")

    @Composable
    override fun View(modifier: Modifier) {
        val (code, setCode) = remember { codeState }
        SmsCodeInput(
            code = code,
            onCodeChanged = setCode,
            phoneNumber = phoneNumber,
            modifier = modifier,
            onBackClicked = ::navigateUp,
        )
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
) {
    val unbreakablePhoneNumber by remember(phoneNumber) {
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
            text = "Код подтверждения",
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
            "Мы отправили код подтверждения на номер $unbreakablePhoneNumber",
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
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp),
            colors = ButtonDefaults.textButtonColors(
                // TODO: replace later with theme value
                contentColor = Color(0xFF18191B)
            )
        ) {
            Text(
                "Войти с помощью пароля",
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