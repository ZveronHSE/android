package ru.zveron.authorization.phone.phone_input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.authorization.R
import ru.zveron.authorization.phone.phone_input.deps.PhoneInputNavigator
import ru.zveron.authorization.phone.phone_input.ui.PhoneInputState
import ru.zveron.authorization.phone.phone_input.ui.PhoneInputViewModel
import ru.zveron.design.components.ActionButton
import ru.zveron.design.inputs.PhoneTextField
import ru.zveron.design.shimmering.shimmeringBackground
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.enabledButtonGradient

internal class PhoneInputNode(
    buildContext: BuildContext,
    private val phoneInputNavigator: PhoneInputNavigator,
    rootScope: Scope,
    private val phoneInputComponent: PhoneInputComponent = PhoneInputComponent(),
) : ViewModelNode(
    buildContext = buildContext,
    plugins = listOf(phoneInputComponent),
) {

    init {
        phoneInputComponent.scope.linkTo(rootScope)
    }

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<PhoneInputViewModel>(
            scope = phoneInputComponent.scope,
            parameters = { parametersOf(phoneInputNavigator) },
            viewModelStoreOwner = this,
        )

        val textState = remember { viewModel.textState }
        val state = viewModel.stateFlow.collectAsState()

        val isContinueButtonEnabled by remember {
            derivedStateOf {
                viewModel.canContinue(textState.value, state.value)
            }
        }

        PhoneInput(
            state = state.value,
            text = textState.value,
            onTextChanged = { textState.value = it },
            modifier = modifier,
            onBackClicked = ::navigateUp,
            onContinueClicked = viewModel::continueClicked,
            onPasswordClicked = phoneInputNavigator::navigateToPasswordScreen,
            isContinueButtonEnabled = isContinueButtonEnabled,
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun PhoneInput(
    state: PhoneInputState,
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onContinueClicked: () -> Unit = {},
    onPasswordClicked: () -> Unit = {},
    isContinueButtonEnabled: Boolean = true,
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
                    color = if (state.isError) {
                        MaterialTheme.colors.error
                    } else {
                        Color.Unspecified
                    },
                )
            )
        }

        ActionButton(
            onClick = onContinueClicked,
            enabled = isContinueButtonEnabled,
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

            if (state.isLoading) {
                Box(Modifier.fillMaxSize().shimmeringBackground(this.maxWidth))
            }
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

        val state = PhoneInputState()

        PhoneInput(state, text, changeText, modifier = Modifier.fillMaxSize())
    }
}