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
import androidx.compose.ui.res.painterResource
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
) : Node(buildContext = buildContext) {
    @Composable
    override fun View(modifier: Modifier) {
        PhoneInput(modifier)
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun PhoneInput(
    modifier: Modifier = Modifier,
) {
    val (text, changeText) = remember {
        mutableStateOf("")
    }

    Column(modifier = modifier.fillMaxSize()) {
        IconButton(
            onClick = { /*TODO*/ },
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
                "Номер телефона",
                style = TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 34.sp,
                    letterSpacing = 0.36.sp,
                    fontWeight = FontWeight.Medium,
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                "Отправим SMS с кодом подтверждения",
                style = TextStyle(
                    fontSize = 13.sp,
                    lineHeight = 15.23.sp,
                    fontWeight = FontWeight.Normal,
                )
            )

            Spacer(Modifier.height(24.dp))

            PhoneTextField(
                countryCodePrefix = "+7 ",
                text = text,
                onTextChanged = changeText,
                textStyle = TextStyle(
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    letterSpacing = 0.36.sp,
                    fontWeight = FontWeight.Medium,
                )
            )
        }

        ActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(
                "Продолжить",
                style = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = 18.75.sp,
                    fontWeight = FontWeight.Normal,
                )
            )
        }

        Spacer(Modifier.height(8.dp))

        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp),
        ) {
            Text(
                "Войти с помощью пароля",
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
        PhoneInput(modifier = Modifier.fillMaxSize())
    }
}