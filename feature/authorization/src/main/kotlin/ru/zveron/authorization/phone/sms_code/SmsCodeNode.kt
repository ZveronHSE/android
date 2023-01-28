package ru.zveron.authorization.phone.sms_code

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zveron.authorization.R
import ru.zveron.authorization.phone.sms_code.deps.SmsCodeDeps
import ru.zveron.authorization.phone.sms_code.ui.CodeRequestState
import ru.zveron.authorization.phone.sms_code.ui.SmsCodeState
import ru.zveron.authorization.phone.sms_code.ui.SmsCodeViewModel
import ru.zveron.design.inputs.SmsCodeTxtField
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.enabledButtonGradient

class SmsCodeNode(
    buildContext: BuildContext,
    private val smsCodeDeps: SmsCodeDeps,
) : Node(buildContext = buildContext) {

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<SmsCodeViewModel>(
            parameters = { parametersOf(smsCodeDeps) },
        )

        LaunchedEffect(viewModel) {
            viewModel.launchTicker()
        }

        val state by viewModel.stateFlow.collectAsState()

        val code by remember { viewModel.codeState }
        val phone by remember { viewModel.phoneNumberState }

        SmsCodeInput(
            state = state,
            code = code,
            onCodeChanged = viewModel::onCodeChanged,
            phoneNumber = phone,
            modifier = modifier,
            onBackClicked = ::navigateUp,
            onPasswordClicked = viewModel::passwordClicked,
            onRequestCodeClicked = viewModel::requestCodeClicked,
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SmsCodeInput(
    state: SmsCodeState,
    code: String,
    onCodeChanged: (String) -> Unit,
    phoneNumber: String,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onPasswordClicked: () -> Unit = {},
    onRequestCodeClicked: () -> Unit = {},
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

        val dividerColor = if (!state.isError) Color(0x4D000000) else Color(0xFFFF4949)
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
            dividerColor = dividerColor,
        )

        if (state.isError) {
            Spacer(Modifier.height(4.dp))

            ErrorRow(modifier = Modifier.padding(start = 12.dp))
        }
        
        Spacer(Modifier.height(20.dp))

        RequestCodeContainer(
            codeState = state.codeRequestState,
            onRequestCodeClicked = onRequestCodeClicked,
            modifier = Modifier.padding(start = 8.dp),
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

@OptIn(ExperimentalTextApi::class)
@Composable
private fun RequestCodeContainer(
    codeState: CodeRequestState,
    onRequestCodeClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (codeState) {
        CodeRequestState.CanRequestCode -> TextButton(onClick = onRequestCodeClicked) {
            Text(
                stringResource(R.string.sms_request_code_button_label),
                style = TextStyle(
                    brush = enabledButtonGradient,
                    fontSize = 14.sp,
                    lineHeight = 16.41.sp,
                    fontWeight = FontWeight.Medium,
                ),
                modifier = modifier,
            )
        }
        is CodeRequestState.NeedToWait -> {
            Text(
                stringResource(id = R.string.sms_request_code_timer, codeState.secondsLeft),
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 16.41.sp,
                    color = Color(0xFF666770),
                ),
                modifier = modifier.padding(start = 8.dp),
            )
        }
    }
}

@Composable
private fun ErrorRow(
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_error_alert),
            // TODO: add accessibility
            contentDescription = null,
            tint = Color(0xFFFF4949),
        )

        Spacer(Modifier.width(4.dp))

        Text(
            stringResource(R.string.sms_error_label),
            style = TextStyle(
                fontSize = 13.sp,
                lineHeight = 15.23.sp,
                fontWeight = FontWeight.Normal,
            ),
            color = Color(0xFFFF4949),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSmsCode() {
    val state = SmsCodeState()

    ZveronTheme {
        val (code, setCode) = remember { mutableStateOf("") }

        SmsCodeInput(
            state = state,
            code = code,
            onCodeChanged = setCode,
            phoneNumber = "+7 (999) 999-99-99",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSmsCodeDelay() {
    val state = SmsCodeState(
        codeRequestState = CodeRequestState.NeedToWait(29)
    )

    ZveronTheme {
        val (code, setCode) = remember { mutableStateOf("") }

        SmsCodeInput(
            state = state,
            code = code,
            onCodeChanged = setCode,
            phoneNumber = "+7 (999) 999-99-99",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSmsCodeError() {
    val state = SmsCodeState(
        codeRequestState = CodeRequestState.NeedToWait(29),
        isError = true,
    )

    ZveronTheme {
        val (code, setCode) = remember { mutableStateOf("") }

        SmsCodeInput(
            state = state,
            code = code,
            onCodeChanged = setCode,
            phoneNumber = "+7 (999) 999-99-99",
            modifier = Modifier.fillMaxSize(),
        )
    }
}