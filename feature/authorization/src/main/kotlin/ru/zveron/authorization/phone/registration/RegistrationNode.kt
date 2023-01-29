package ru.zveron.authorization.phone.registration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zveron.authorization.R
import ru.zveron.authorization.phone.registration.ui.RegistrationViewModel
import ru.zveron.design.components.ActionButton
import ru.zveron.design.shimmering.shimmeringBackground
import ru.zveron.design.theme.ZveronTheme

class RegistrationNode(
    private val phone: String,
    buildContext: BuildContext,
) : Node(buildContext = buildContext) {

    @Composable
    override fun View(modifier: Modifier) {
        val viewModel = koinViewModel<RegistrationViewModel>(
            parameters = { parametersOf(phone) },
        )

        val (username, changeUsername) = remember { viewModel.usernameState }
        val (password, changePassword) = remember { viewModel.passwordState }

        val state by viewModel.registrationUiState.collectAsState()

        LaunchedEffect(viewModel) {
            viewModel.finishRegistrationFlow.collect {
                this@RegistrationNode.finish()
            }
        }

        Registration(
            isLoading = state.isLoading,
            phone = username,
            onPhoneChanged = changeUsername,
            password = password,
            onPasswordChanged = changePassword,
            onBackClicked = ::navigateUp,
            onRegisterClicked = viewModel::register,
            modifier = modifier,
        )
    }
}

@Composable
private fun Registration(
    isLoading: Boolean,
    phone: String,
    onPhoneChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onRegisterClicked: () -> Unit = {},
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
            stringResource(R.string.last_step_label),
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
            stringResource(R.string.name_label),
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
            placeholder = { Text(stringResource(R.string.name_placeholder)) },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 34.dp),
        )

        Spacer(Modifier.height(16.dp))

        Text(
            stringResource(R.string.password_label),
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
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 34.dp),
        )

        Spacer(Modifier.height(6.dp))
        Text(
            stringResource(R.string.password_explanation),
            style = TextStyle(
                color = Color(0xFF929292),
                fontSize = 13.sp,
                lineHeight = 15.23.sp,
                fontWeight = FontWeight.Normal,
            ),
            modifier = Modifier.padding(start = 16.dp),
        )

        Spacer(Modifier.weight(1f))

        ActionButton(
            onClick = onRegisterClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
        ) {
            Text(
                stringResource(R.string.register),
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 18.75.sp,
                    fontWeight = FontWeight.Normal,
                ),
            )

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmeringBackground(this.maxWidth)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrationPreview() {
    val (phone, changePhone) = remember {
        mutableStateOf("")
    }

    val (password, changePassword) = remember {
        mutableStateOf("")
    }

    ZveronTheme {
        Registration(
            isLoading = false,
            phone = phone,
            onPhoneChanged = changePhone,
            password = password,
            onPasswordChanged = changePassword,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegistrationPreviewLoading() {
    val (phone, changePhone) = remember {
        mutableStateOf("")
    }

    val (password, changePassword) = remember {
        mutableStateOf("")
    }

    ZveronTheme {
        Registration(
            isLoading = true,
            phone = phone,
            onPhoneChanged = changePhone,
            password = password,
            onPasswordChanged = changePassword,
        )
    }
}