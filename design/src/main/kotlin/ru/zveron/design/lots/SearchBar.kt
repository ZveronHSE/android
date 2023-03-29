package ru.zveron.design.lots

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.zveron.design.R
import ru.zveron.design.theme.ZveronTheme
import ru.zveron.design.theme.enabledButtonGradient
import ru.zveron.design.theme.gray1
import ru.zveron.design.theme.gray3

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    inputHint: String,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    leadFrame: @Composable () -> Unit = {
        Icon(
            painterResource(R.drawable.ic_search),
            null,
            tint = gray3,
        )
    },
    trailFrame: (@Composable () -> Unit)? = null,
    alwaysKeepTrail: Boolean = false,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        interactionSource = interactionSource,
        textStyle = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = gray3,
        ),
        cursorBrush = enabledButtonGradient,
        modifier = modifier,
    ) { innerTextField ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(gray1)
                .padding(10.dp),
        ) {
            val isFocused = interactionSource.collectIsFocusedAsState()

            Spacer(Modifier.width(8.dp))

            leadFrame.invoke()

            Spacer(Modifier.width(8.dp))

            if (value.isEmpty() && !isFocused.value) {
                Text(
                    text = inputHint,
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = gray3,
                    )
                )
            }

            innerTextField.invoke()

            Spacer(modifier = Modifier.weight(1f))

            if (alwaysKeepTrail || value.isNotEmpty()) {
                trailFrame?.invoke()
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF6650a4)
@Composable
private fun PreviewClear() {
    ZveronTheme {
        val (input, setInput) = remember {
            mutableStateOf("")
        }

        SearchBar(
            value = input,
            onValueChange = setInput,
            inputHint = "Поиск",
            trailFrame = {
                IconButton(onClick = { setInput("") }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close_gradient),
                        contentDescription = stringResource(R.string.search_clear_hint),
                        tint = Color.Unspecified,
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF6650a4)
@Composable
private fun PreviewButton() {
    ZveronTheme {
        val (input, setInput) = remember {
            mutableStateOf("")
        }

        SearchBar(
            value = input,
            onValueChange = setInput,
            inputHint = "Поиск",
            alwaysKeepTrail = true,
            trailFrame = {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.clickable(
                        onClickLabel = null,
                        role = Role.Button,
                        onClick = {  },
                    ),
                )
            }
        )
    }
}