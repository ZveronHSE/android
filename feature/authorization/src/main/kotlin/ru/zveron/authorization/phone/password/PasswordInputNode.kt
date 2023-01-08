package ru.zveron.authorization.phone.password

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import ru.zveron.design.components.ActionButton
import ru.zveron.design.theme.ZveronTheme

class PasswordInputNode(
    buildContext: BuildContext,
) : Node(buildContext = buildContext) {
    private val phoneState = mutableStateOf("")
    private val passwordState = mutableStateOf("")

    @Composable
    override fun View(modifier: Modifier) {
        val (phone, changePhone) = remember { phoneState }
        val (password, changePassword) = remember { passwordState }

        PasswordInput(
            phone = phone,
            onPhoneChanged = changePhone,
            password = password,
            onPasswordChanged = changePassword,
            onBackClicked = ::navigateUp,
            modifier = modifier,
        )
    }
}

@Composable
private fun PasswordInput(
    phone: String,
    onPhoneChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
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
            "Вход",
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
            "Номер телефона",
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
            "Пароль",
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

        Spacer(Modifier.weight(1f))

        ActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
        ) {
            Text(
                "Продолжить",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 18.75.sp,
                    fontWeight = FontWeight.Normal,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PasswordPreview() {
    val (phone, changePhone) = remember {
        mutableStateOf("")
    }

    val (password, changePassword) = remember {
        mutableStateOf("")
    }

    ZveronTheme {
        PasswordInput(
            phone = phone,
            onPhoneChanged = changePhone,
            password = password,
            onPasswordChanged = changePassword,
        )
    }
}