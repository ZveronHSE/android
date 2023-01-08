package ru.zveron.design.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import ru.zveron.design.theme.ZveronTheme
import kotlin.math.min

private const val PHONE_NUMBER_LENGTH = 10
private const val mask = "000 000-00-00"

@Composable
fun PhoneTextField(
    text: String,
    countryCodePrefix: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    hintColor: Color = Color(0xFFACAEB4),
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black)
) {
    BasicTextField(
        value = text,
        onValueChange = onTextChanged,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = PhoneVisualTransformation(countryCodePrefix, hintColor),
        singleLine = true,
        maxLines = 1,
        decorationBox = { innerTextField -> innerTextField() },
        modifier = modifier,

        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardActions = keyboardActions,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
    )
}

internal class PhoneVisualTransformation(
    private val prefix: String,
    private val hintColor: Color,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= PHONE_NUMBER_LENGTH) {
            text.subSequence(0, PHONE_NUMBER_LENGTH)
        } else {
            text
        }

        val annotatedString = buildAnnotatedString {
            append(prefix)
            for (i in trimmed.indices) {
                if (i == 3) {
                    append(" ")
                } else if (i == 6 || i == 8) {
                    append("-")
                }
                append(trimmed[i])
            }
            withStyle(SpanStyle(color = hintColor)) {
                append(mask.takeLast(mask.length - length + prefix.length))
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return run {
                    if (offset <= 2) {
                        offset // xxx 000-00-00
                    } else if (offset <= 5) {
                        offset + 1 // xxx_xxx-00-00
                    } else if (offset <= 7) {
                        offset + 2 // xxx_xxx_xx-00
                    } else if (offset < PHONE_NUMBER_LENGTH) {
                        offset + 3 // xxx_xxx_xx_xx
                    } else mask.length
                } + prefix.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                val calculatedOffset = run {
                    val offsetWithoutPrefix = offset - prefix.length
                    if (offsetWithoutPrefix < 0) {
                        0
                    } else if (offsetWithoutPrefix <= 3) {
                        offsetWithoutPrefix
                    } else if (offsetWithoutPrefix <= 7) {
                        offsetWithoutPrefix - 1
                    } else if (offsetWithoutPrefix <= 10) {
                        offsetWithoutPrefix - 2
                    } else if (offsetWithoutPrefix < mask.length) {
                        offsetWithoutPrefix - 3
                    } else mask.length
                }
                return min(calculatedOffset, text.length)
            }

        }

        return TransformedText(annotatedString, offsetMapping)
    }
}

@Preview
@Composable
private fun PhoneTextFieldPreview() {
    ZveronTheme {
        var text by remember {
            mutableStateOf("")
        }
        PhoneTextField(
            text = text,
            countryCodePrefix = "+7 ",
            onTextChanged = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background),
        )
    }
}