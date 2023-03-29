package ru.zveron.authorization.phone.password

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.core.modality.BuildContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import ru.zveron.appyx.viewmodel.ViewModelNode
import ru.zveron.authorization.R
import ru.zveron.authorization.phone.password.deps.PasswordNavigator
import ru.zveron.authorization.phone.password.ui.PasswordUiState
import ru.zveron.authorization.phone.password.ui.PasswordViewModel
import ru.zveron.authorization.phone.password.ui.PhoneCodeVisualTransformation
import ru.zveron.design.components.ActionButton
import ru.zveron.design.shimmering.shimmeringBackground
import ru.zveron.design.theme.ZveronTheme

internal class PasswordInputNode(
    buildContext: BuildContext,
    scope: Scope,
    private val passwordInputComponent: PasswordInputComponent = PasswordInputComponent(),
    private val passwordNavigator: PasswordNavigator,
) : ViewModelNode(
    buildContext = buildContext,
    plugins = listOf(passwordInputComponent),
) {

    init {
        passwordInputComponent.scope.linkTo(scope)
    }

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<PasswordViewModel>(
            scope = passwordInputComponent.scope,
            parameters = { parametersOf(passwordNavigator) },
            viewModelStoreOwner = this,
        )

        val state = viewModel.stateFlow.collectAsState()

        val phoneState = remember { viewModel.phoneState }
        val passwordState = remember { viewModel.passwordState }

        val continueButtonEnabled by remember {
            derivedStateOf {
                viewModel.canLogin(phoneState.value, passwordState.value, state.value)
            }
        }

        LaunchedEffect(viewModel) {
            viewModel.finishRegistrationFlow.collect {
                this@PasswordInputNode.finish()
            }
        }

        PasswordInput(
            state = state.value,
            phone = phoneState.value,
            onPhoneChanged = {
                if (it.length <= 10) {
                    phoneState.value = it
                }
            },
            password = passwordState.value,
            onPasswordChanged = { passwordState.value = it },
            onBackClicked = ::navigateUp,
            onContinueClicked = viewModel::login,
            continueButtonEnabled = continueButtonEnabled,
            modifier = modifier.windowInsetsPadding(WindowInsets.safeDrawing),
        )
    }
}

@Composable
private fun PasswordInput(
    state: PasswordUiState,
    phone: String,
    onPhoneChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onContinueClicked: () -> Unit = {},
    continueButtonEnabled: Boolean = true,
) {
    Column(modifier = modifier.fillMaxSize()) {
        IconButton(
            onClick = onBackClicked,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp, start = 4.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_back),
                // TODO: fix accessibility
                contentDescription = null,
            )
        }

        Spacer(Modifier.height(34.dp))

        Text(
            stringResource(R.string.login_label),
            style = TextStyle(
                fontSize = 24.sp,
                lineHeight = 34.sp,
                letterSpacing = 0.36.sp,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier.padding(start = 16.dp),
        )

        Spacer(Modifier.height(16.dp))

        Text(
            stringResource(R.string.phone_number),
            style = TextStyle(
                color = Color(0xFF929292),
                fontSize = 13.sp,
                lineHeight = 15.23.sp,
                fontWeight = FontWeight.Normal,
            ),
            modifier = Modifier.padding(start = 16.dp),
        )
        Spacer(Modifier.height(6.dp))
        TextField(
            value = phone,
            onValueChange = onPhoneChanged,
            placeholder = { Text(stringResource(R.string.phone_placeholder)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            visualTransformation = PhoneCodeVisualTransformation(
                stringResource(R.string.phone_placeholder)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            isError = state.isError,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 34.dp),
        )

        Spacer(Modifier.height(16.dp))

        Text(
            stringResource(R.string.password),
            style = TextStyle(
                color = Color(0xFF929292),
                fontSize = 13.sp,
                lineHeight = 15.23.sp,
                fontWeight = FontWeight.Normal,
            ),
            modifier = Modifier.padding(start = 16.dp),
        )
        Spacer(Modifier.height(6.dp))
        TextField(
            value = password,
            onValueChange = onPasswordChanged,
            placeholder = { Text(stringResource(R.string.password_placeholder)) },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            isError = state.isError,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 34.dp),
        )

        Spacer(Modifier.weight(1f))

        ActionButton(
            onClick = onContinueClicked,
            enabled = continueButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
        ) {
            Text(
                stringResource(R.string.login_button_label),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 18.75.sp,
                    fontWeight = FontWeight.Normal,
                ),
            )

            if (state.isLoading) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .shimmeringBackground(this.maxWidth))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordPreview() {
    val state = PasswordUiState(isError = false)

    val (phone, changePhone) = remember {
        mutableStateOf("")
    }

    val (password, changePassword) = remember {
        mutableStateOf("")
    }

    val continueButtonEnabled by remember {
        derivedStateOf {
            phone.length == 10 && !state.isLoading
        }
    }

    ZveronTheme {
        PasswordInput(
            state = state,
            phone = phone,
            onPhoneChanged = changePhone,
            password = password,
            onPasswordChanged = changePassword,
            continueButtonEnabled = continueButtonEnabled,
        )
    }
}