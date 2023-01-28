package ru.zveron.design.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.theme.ZveronTheme

@Composable
fun OtpTextField(
    code: String,
    onCodeChanged: (String) -> Unit,
    maxCodeLength: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current.copy(
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    ),
    contentColor: Color = LocalContentColor.current.copy(LocalContentAlpha.current),
    dividerColor: Color = MaterialTheme.colors.primary,
) {
    val codeChanger = remember<(String) -> Unit> {
        { if (it.length <= maxCodeLength) onCodeChanged.invoke(it) }
    }

    BasicTextField(
        value = code,
        onValueChange = codeChanger,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            repeat(maxCodeLength) { index ->
                OtpChar(
                    index = index,
                    text = code,
                    contentColor = contentColor,
                    dividerColor = dividerColor,
                    textStyle = textStyle,
                )
            }
        }
    }
}

@Composable
private fun OtpChar(
    index: Int,
    text: String,
    contentColor: Color,
    dividerColor: Color,
    textStyle: TextStyle,
) {
    val char = when {
        index >= text.length -> ""
        else -> text[index].toString()
    }

    val isContentHighlighted = index < text.length
    val actualContentColor = if (isContentHighlighted) contentColor else contentColor.copy(alpha = 0.3f)

    val isDividerHighlighted = index <= text.length
    val actualDividerColor = if (isDividerHighlighted) dividerColor else dividerColor.copy(alpha = 0.3f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(24.dp),
    ) {
        Text(
            text = char,
            style = textStyle,
            color = actualContentColor,
        )

        Box(
            modifier = Modifier
                .width(20.dp)
                .padding(bottom = 2.dp)
                .height(2.dp)
                .background(actualDividerColor, RoundedCornerShape(2.dp))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OOO() {
    ZveronTheme {
        val (code, setCode) = remember { mutableStateOf("") }

        OtpTextField(code, setCode, maxCodeLength = 5)
    }
}
