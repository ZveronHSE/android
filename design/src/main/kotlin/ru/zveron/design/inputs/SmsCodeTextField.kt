package ru.zveron.design.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.theme.ZveronTheme

@ExperimentalComposeUiApi
@Composable
fun SmsCodeTxtField(
    code: String,
    onCodeChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    dividerColor: Color = MaterialTheme.colors.primary,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    ),
) {
    val focusList = remember {
        val (item1, item2, item3, item4, item5) = FocusRequester.createRefs()
        listOf(item1, item2, item3, item4, item5)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        for (i in 0 until 5) {
            CodeCharTextField(
                text = code.getOrNull(i)?.toString().orEmpty(),
                onTextChanged = { digit ->
                    if (i == code.length) {
                        onCodeChanged(code + digit)
                    } else if (digit.isNotEmpty()) {
                        onCodeChanged(code.replaceRange(i, i, digit))
                    } else {
                        onCodeChanged(code.removeRange(i, i + 1))
                    }
                },
                dividerColor = dividerColor,
                enabled = i <= code.length,
                textStyle = textStyle,
                modifier = Modifier
                    .focusRequester(focusList[i])
                    .focusProperties {
                        next = focusList.getOrNull(i + 1) ?: focusList[i]
                        previous = focusList.getOrNull(i - 1) ?: focusList[i]
                    }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CodeCharTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    dividerColor: Color = MaterialTheme.colors.primary,
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    ),
) {
    val pattern = remember { Regex("^[^\\t]*\$") } //to not accept the tab key as value
    val maxCharAmount = 1
    val focusManager = LocalFocusManager.current

    LaunchedEffect(text) {
        if (text.isNotEmpty()) {
            focusManager.moveFocus(
                focusDirection = FocusDirection.Next,
            )
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = text,
            enabled = enabled,
            onValueChange = {
                if (
                    it.length <= maxCharAmount
                    && (it.isEmpty() || it.matches(pattern))
                ) {
                    onTextChanged(it)
                }
            },
            modifier = modifier
                .width(50.dp)
                .onKeyEvent {
                    if (it.key == Key.Tab) {
                        focusManager.moveFocus(FocusDirection.Next)
                        return@onKeyEvent true
                    }
                    if (text.isEmpty() && it.key == Key.Backspace) {
                        focusManager.moveFocus(FocusDirection.Previous)
                        return@onKeyEvent true
                    }
                    false
                },
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.NumberPassword,
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Transparent,
                unfocusedIndicatorColor = Transparent,
                focusedIndicatorColor = Transparent,
                disabledIndicatorColor = Transparent,
            ),
        )

        Box(
            modifier = Modifier
                .width(20.dp)
                .padding(bottom = 2.dp)
                .offset(y = (-10).dp)
                .height(2.dp)
                .background(dividerColor, RoundedCornerShape(2.dp))
        )
//        Divider(
//            Modifier
//                .width(28.dp)
//                .padding(bottom = 2.dp)
//                .offset(y = (-10).dp),
//            color = dividerColor,
//            thickness = 1.dp
//        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
private fun OOO() {
    ZveronTheme {
        val (code, setCode) = remember { mutableStateOf("") }

        SmsCodeTxtField(code, setCode)
    }
}
